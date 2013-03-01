//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/crs/transformations/coordinate/ConcatenatedTransform.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de
 ---------------------------------------------------------------------------*/

package org.deegree.crs.transformations.coordinate;

import java.util.List;

import javax.vecmath.GMatrix;
import javax.vecmath.Point3d;

import org.deegree.crs.Identifiable;
import org.deegree.crs.exceptions.TransformationException;
import org.deegree.crs.transformations.Transformation;

/**
 * The <code>ConcatenatedTransform</code> class allows the connection of two transformations.
 * <p>
 * Calling inverse on this transformation will invert the whole underlying transformation chain. For example, if A * (B
 * *C)=D and D is this transformation calling D.inverse() will result in (C.inverse * B.inverse) * A.inverse.
 * </p>
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14974 $, $Date: 2008-11-20 16:53:04 +0100 (Do, 20. Nov 2008) $
 * 
 */

public class ConcatenatedTransform extends CRSTransformation {

    private boolean isIdentitiy = false;

    private Transformation firstTransform;

    private Transformation secondTransform;

    /**
     * Creates a transform by concatenating two existing transforms. A concatenated transform applies two transforms,
     * one after the other. The dimension of the output space of the first transform must match the dimension of the
     * input space in the second transform.
     * 
     * @param first
     *            The first transformation to apply to given points.
     * @param second
     *            The second transformation to apply to given points.
     * @param id
     *            an identifiable instance containing information about this transformation
     */
    public ConcatenatedTransform( Transformation first, Transformation second, Identifiable id ) {
        super( first.getSourceCRS(), second.getTargetCRS(), id );
        if ( first.isIdentity() && second.isIdentity() ) {
            isIdentitiy = true;
        }
        firstTransform = first;
        secondTransform = second;
    }

    /**
     * Creates a transform by concatenating two existing transforms. A concatenated transform applies two transforms,
     * one after the other. The dimension of the output space of the first transform must match the dimension of the
     * input space in the second transform.
     * 
     * Creates an Identifiable using the {@link CRSTransformation#createFromTo(String, String)} method.
     * 
     * @param first
     *            The first transformation to apply to given points.
     * @param second
     *            The second transformation to apply to given points.
     */
    public ConcatenatedTransform( Transformation first, Transformation second ) {
        this( first, second, new Identifiable( Transformation.createFromTo( first.getIdentifier(),
                                                                            second.getIdentifier() ) ) );
    }

    @Override
    public List<Point3d> doTransform( List<Point3d> srcPts )
                            throws TransformationException {
        if ( !isIdentitiy ) {
            List<Point3d> dest = firstTransform.doTransform( srcPts );
            return secondTransform.doTransform( dest );
        }
        return srcPts;
    }

    @Override
    public void inverse() {
        super.inverse();
        Transformation tmp = firstTransform;
        firstTransform = secondTransform;
        secondTransform = tmp;
        firstTransform.inverse();
        secondTransform.inverse();
    }

    @Override
    public boolean isIdentity() {
        return isIdentitiy;
    }

    /**
     * @return the firstTransform, which is the second transformation if this transform is inverse.
     */
    public final Transformation getFirstTransform() {
        return firstTransform;
    }

    /**
     * @return the secondTransform, which is the first transformation if this transform is inverse.
     */
    public final Transformation getSecondTransform() {
        return secondTransform;
    }

    @Override
    public String getImplementationName() {
        return "Concatenated-Transform";
    }

    /**
     * Concatenates two existing transforms.
     * 
     * @param first
     *            The first transform to apply to points.
     * @param second
     *            The second transform to apply to points.
     * @param keepIdentity
     *            true if identity {@link Transformation}s should be kept in the resulting {@link ConcatenatedTransform}
     *            . Default value is <code>false</code>.
     * @return The concatenated transform.
     * 
     */
    private static Transformation concatenateTransformations( Transformation first, Transformation second,
                                                              final boolean keepIdentity ) {
        if ( first == null ) {
            return second;
        }
        if ( second == null ) {
            return first;
        }
        // if one of the two is an identity transformation, just return the other.
        if ( !keepIdentity && first.isIdentity() ) {
            return second;
        }
        if ( !keepIdentity && second.isIdentity() ) {
            return first;
        }

        /*
         * If one transform is the inverse of the other, just return an identitiy transform.
         */
        if ( first.areInverse( second ) ) {
            return null;
        }

        /*
         * If both transforms use matrix, then we can create a single transform using the concatened matrix.
         */
        if ( first instanceof MatrixTransform && second instanceof MatrixTransform ) {
            GMatrix m1 = ( (MatrixTransform) first ).getMatrix();
            GMatrix m2 = ( (MatrixTransform) second ).getMatrix();
            if ( m1 == null ) {
                if ( m2 == null ) {
                    // both matrices are null, just return the identiy matrix.
                    return new MatrixTransform( first.getSourceCRS(), first.getTargetCRS(),
                                                new GMatrix( second.getTargetDimension() + 1,
                                                             first.getSourceDimension() + 1 ) );
                }
                return second;
            } else if ( m2 == null ) {
                return first;
            }
            if ( m2.getNumCol() != m1.getNumCol() || m2.getNumRow() != m1.getNumRow() ) {
                return new ConcatenatedTransform( first, second );
            }
            m2.mul( m1 );

            // m1.mul( m2 );
            MatrixTransform result = new MatrixTransform( first.getSourceCRS(), second.getTargetCRS(), m2 );
            // if ( result.isIdentity() ) {
            // return null;
            // }
            return result;
        }

        /*
         * If one or both math transform are instance of {@link ConcatenatedTransform}, then concatenate
         * <code>tr1</code> or <code>tr2</code> with one of step transforms.
         */
        if ( first instanceof ConcatenatedTransform ) {
            final ConcatenatedTransform ctr = (ConcatenatedTransform) first;
            first = ctr.getFirstTransform();
            second = concatenateTransformations( ctr.getSecondTransform(), second, keepIdentity );
        } else if ( second instanceof ConcatenatedTransform ) {
            final ConcatenatedTransform ctr = (ConcatenatedTransform) second;
            first = concatenateTransformations( first, ctr.getFirstTransform(), keepIdentity );
            second = ctr.getSecondTransform();
        }
        // because of the concatenation one of the transformations may be null.
        if ( first == null ) {
            return second;
        }
        if ( second == null ) {
            return first;
        }

        return new ConcatenatedTransform( first, second );
    }

    /**
     * Concatenate two transformations.
     * 
     * @param step1
     *            The first step, or <code>null</code> for the identity transform.
     * @param step2
     *            The second step, or <code>null</code> for the identity transform.
     * @param keepIdentity
     *            true if identity {@link Transformation}s should be kept in the resulting {@link ConcatenatedTransform}
     *            . Default value is <code>false</code>.
     * @return A concatenated transform, or <code>null</code> if all arguments was nul.
     */
    public static Transformation concatenate( final Transformation step1, final Transformation step2,
                                              final boolean keepIdentity ) {
        if ( step1 == null ) {
            return step2;
        }
        if ( step2 == null ) {
            return step1;
        }
        return concatenateTransformations( step1, step2, keepIdentity );
    }

    /**
     * Concatenate two transformations.
     * 
     * @param step1
     *            The first step, or <code>null</code> for the identity transform.
     * @param step2
     *            The second step, or <code>null</code> for the identity transform.
     * @return A concatenated transform, or <code>null</code> if all arguments was nul.
     */
    public static Transformation concatenate( final Transformation step1, final Transformation step2 ) {
        return concatenate( step1, step2, false );
    }

    /**
     * Concatenate three transformations into one.
     * 
     * @param step1
     *            The first step, or <code>null</code> for the identity transform.
     * @param step2
     *            The second step, or <code>null</code> for the identity transform.
     * @param step3
     *            The third step, or <code>null</code> for the identity transform.
     * @param keepIdentity
     *            true if identity {@link Transformation}s should be kept in the resulting {@link ConcatenatedTransform}
     *            . Default value is <code>false</code>.
     * @return A concatenated transform, or <code>null</code> if all arguments were <code>null</code>.
     */
    public static Transformation concatenate( final Transformation step1, final Transformation step2,
                                              final Transformation step3, final boolean keepIdentity ) {
        if ( step1 == null ) {
            return concatenate( step2, step3, keepIdentity );
        }
        if ( step2 == null ) {
            return concatenate( step1, step3, keepIdentity );
        }
        if ( step3 == null ) {
            return concatenate( step1, step2, keepIdentity );
        }
        return concatenateTransformations( step1, concatenateTransformations( step2, step3, keepIdentity ),
                                           keepIdentity );
    }

    /**
     * Concatenate three transformations into one.
     * 
     * @param step1
     *            The first step, or <code>null</code> for the identity transform.
     * @param step2
     *            The second step, or <code>null</code> for the identity transform.
     * @param step3
     *            The third step, or <code>null</code> for the identity transform.
     * @return A concatenated transform, or <code>null</code> if all arguments were <code>null</code>.
     */
    public static Transformation concatenate( final Transformation step1, final Transformation step2,
                                              final Transformation step3 ) {
        return concatenate( step1, step2, step3, false );
    }

}
