//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/crs/utilities/Matrix.java $
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
package org.deegree.crs.utilities;

import static org.deegree.crs.projections.ProjectionUtils.EPS11;

import java.awt.geom.AffineTransform;

import javax.vecmath.GMatrix;
import javax.vecmath.Matrix3d;

import org.deegree.crs.components.Axis;
import org.deegree.crs.coordinatesystems.CoordinateSystem;
import org.deegree.crs.coordinatesystems.GeographicCRS;
import org.deegree.crs.exceptions.TransformationException;

/**
 * The <code>Matrix</code> class TODO add documentation here
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14556 $, $Date: 2008-10-29 17:59:04 +0100 (Mi, 29. Okt 2008) $
 * 
 */

public class Matrix extends GMatrix {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3778102551617232269L;

    /**
     * Construct a square identity matrix of size <code>size</code>&nbsp;&times;&nbsp;<code>size</code>.
     * 
     * @param size
     */
    public Matrix( final int size ) {
        super( size, size );
    }

    /**
     * Construct a matrix of size <code>numRow</code>&nbsp;&times;&nbsp;<code>numCol</code>. Elements on the diagonal
     * <var>j==i</var> are set to 1.
     * 
     * @param numRow
     * @param numCol
     */
    public Matrix( final int numRow, final int numCol ) {
        super( numRow, numCol );
    }

    /**
     * Constructs a <code>numRow</code>&nbsp;&times;&nbsp;<code>numCol</code> matrix initialized to the values in the
     * <code>matrix</code> array. The array values are copied in one row at a time in row major fashion. The array
     * should be exactly <code>numRow*numCol</code> in length. Note that because row and column numbering begins with
     * zero, <code>row</code> and <code>numCol</code> will be one larger than the maximum possible matrix index values.
     * 
     * @param numRow
     * @param numCol
     * @param matrix
     */
    public Matrix( final int numRow, final int numCol, final double[] matrix ) {
        super( numRow, numCol, matrix );
        if ( numRow * numCol != matrix.length ) {
            throw new IllegalArgumentException( String.valueOf( matrix.length ) );
        }
    }

    /**
     * Constructs a new matrix from a two-dimensional array of doubles.
     * 
     * @param matrix
     *            Array of rows. Each row must have the same length.
     * @throws IllegalArgumentException
     *             if the specified matrix is not regular (i.e. if all rows doesn't have the same length).
     */
    public Matrix( final double[][] matrix ) throws IllegalArgumentException {
        super( matrix.length, ( matrix.length != 0 ) ? matrix[0].length : 0 );
        final int numRow = getNumRow();
        final int numCol = getNumCol();
        for ( int j = 0; j < numRow; j++ ) {
            if ( matrix[j].length != numCol ) {
                throw new IllegalArgumentException( "Not a regular Matrix (given rows have different lengths)" );
            }
            setRow( j, matrix[j] );
        }
    }

    /**
     * Constructs a new matrix and copies the initial values from the parameter matrix.
     * 
     * @param matrix
     */
    public Matrix( final GMatrix matrix ) {
        super( matrix );
    }

    /**
     * Construct a 3&times;3 matrix from the specified affine transform.
     * 
     * @param transform
     */
    public Matrix( final AffineTransform transform ) {
        super( 3, 3, new double[] { transform.getScaleX(), transform.getShearX(), transform.getTranslateX(),
                                   transform.getShearY(), transform.getScaleY(), transform.getTranslateY(), 0, 0, 1 } );
    }

    /**
     * Construct an affine transform changing axis order. The resulting affine transform will convert incoming
     * coordinates into the given destination Axis. For example if source axis are given with (NORTH,WEST) and
     * destination axis as (EAST,NORTH) assuming the axis use the same units, the resulted matrix will look like:<br/>
     * <code>
     *  &nbsp;0,&nbsp;1,&nbsp;0<br/>
     * -1,&nbsp;0,&nbsp;0<br/>
     *  &nbsp;0,&nbsp;0,&nbsp;1<br/>
     *  </code> Axis orientation can be inverted only. Rotating axis (e.g. from NORTH,WEST, to NORTH,DOWN, ) is not
     * supported.
     * 
     * @param srcAxis
     *            The set of axis orientation for source coordinate system.
     * @param dstAxis
     *            The set of axis orientation for destination coordinate system.
     * @throws IllegalArgumentException
     *             if the affine transform can't be created for some other reason.
     */
    public Matrix( final Axis[] srcAxis, final Axis[] dstAxis ) {
        this( srcAxis.length + 1 );
        final int dimension = srcAxis.length;
        if ( dstAxis.length != dimension ) {
            throw new IllegalArgumentException( "Given dimensions are of differnt length." );
        }
        /*
         * Map source axis to destination axis. If no axis is moved (for example if the user want to transform
         * (NORTH,EAST) to (SOUTH,EAST)), then source and destination index will be equal. If some axis are moved (for
         * example if the user want to transform (NORTH,EAST) to (EAST,NORTH)), then ordinates at index
         * <code>srcIndex</code> will have to be moved at index <code>dstIndex</code>.
         */
        setZero();
        for ( int srcIndex = 0; srcIndex < dimension; srcIndex++ ) {
            boolean hasFound = false;
            final int srcAxe = srcAxis[srcIndex].getOrientation();
            final int sourceAxisDirection = Math.abs( srcAxe );
            for ( int dstIndex = 0; dstIndex < dimension; dstIndex++ ) {
                final int dstAxeDirection = dstAxis[dstIndex].getOrientation();
                if ( sourceAxisDirection == Math.abs( dstAxeDirection ) ) {
                    if ( hasFound ) {
                        throw new IllegalArgumentException( "Following axis are colinear: "
                                                            + srcAxis[srcIndex].getName() + " dstAxe: "
                                                            + dstAxis[dstIndex].getName() );
                    }
                    hasFound = true;
                    // row, column, value
                    setElement( dstIndex, srcIndex, ( srcAxe == dstAxeDirection ) ? 1 : -1 );
                }
            }
            if ( !hasFound ) {
                throw new IllegalArgumentException( "No appropriate transformation axis found for srcAxis: "
                                                    + srcAxis[srcIndex].getName() );
            }
        }
        setElement( dimension, dimension, 1 );

    }

    /**
     * Returns <code>true</code> if this matrix is an affine transform. A transform is affine if the matrix is square
     * and last row contains only zeros, except in the last column which contains 1.
     * 
     * @return <code>true</code> if this matrix is an affine transform.
     */
    public final boolean isAffine() {
        int dimension = getNumRow();
        if ( dimension != getNumCol() ) {
            return false;
        }

        dimension--;
        for ( int i = 0; i <= dimension; i++ ) {
            if ( Math.abs( getElement( dimension, i ) - ( i == dimension ? 1 : 0 ) ) > EPS11 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Copies the first 2x3 values into an affine transform object. If not enough values are available, an identity
     * transform is returned.
     * 
     * @return an affine transform for this matrix. or an identity if this matrix has not sufficient values.
     * 
     */
    public final Matrix3d toAffineTransform() {
        if ( getNumCol() < 3 || getNumRow() < 2 ) {
            return new Matrix3d();
        }
        return new Matrix3d( getElement( 0, 0 ), getElement( 0, 1 ), getElement( 0, 2 ), getElement( 1, 0 ),
                             getElement( 1, 1 ), getElement( 1, 2 ), 0, 0, 1 );
    }

    /**
     * Returns <code>true</code> if this matrix is an identity matrix.
     * 
     * @return <code>true</code> if this matrix is an identity matrix.
     */
    public final boolean isIdentity() {
        final int numRow = getNumRow();
        final int numCol = getNumCol();
        if ( numRow != numCol ) {
            return false;
        }
        for ( int j = 0; j < numRow; j++ )
            for ( int i = 0; i < numCol; i++ ) {
                if ( Math.abs( getElement( j, i ) - ( i == j ? 1 : 0 ) ) > EPS11 ) {
                    return false;
                }
            }
        return true;
    }

    /**
     * @return an affine transform between two geographic coordinate systems. Only units, axis order (e.g. transforming
     *         from (NORTH,WEST) to (EAST,NORTH)) and prime meridian are taken in account. Other attributes (especially
     *         the datum) must be checked before invoking this method.
     * 
     * @param sourceCRS
     *            The source coordinate system.
     * @param targetCRS
     *            The target coordinate system.
     * @throws TransformationException
     *             if some error occurs.
     */
    public static Matrix swapAndRotateGeoAxis( final GeographicCRS sourceCRS, final GeographicCRS targetCRS )
                            throws TransformationException {
        Matrix matrix = swapAxis( sourceCRS, targetCRS );
        if ( !sourceCRS.getGeodeticDatum().getPrimeMeridian().equals( targetCRS.getGeodeticDatum().getPrimeMeridian() ) ) {
            if ( matrix == null ) {
                matrix = new Matrix( sourceCRS.getDimension() + 1 );
            }
            Axis[] targetAxis = targetCRS.getAxis();
            final int lastMatrixColumn = matrix.getNumCol() - 1;
            for ( int i = 0; i < targetAxis.length; ++i ) {
                // Find longitude, and apply a translation if prime meridians are different.
                final int orientation = targetAxis[i].getOrientation();
                if ( Axis.AO_WEST == Math.abs( orientation ) ) {
                    final double sourceLongitude = sourceCRS.getGeodeticDatum().getPrimeMeridian().getLongitudeAsRadian();
                    final double targetLongitude = targetCRS.getGeodeticDatum().getPrimeMeridian().getLongitudeAsRadian();
                    if ( Math.abs( sourceLongitude - targetLongitude ) > EPS11 ) {
                        double translation = targetLongitude - sourceLongitude;
                        if ( Axis.AO_WEST == orientation ) {
                            translation = -translation;
                        }
                        // add the translation to the matrix translate element of this axis
                        matrix.setElement( i, lastMatrixColumn, matrix.getElement( i, lastMatrixColumn ) - translation );

                    }
                }
            }
        }
        return matrix;
    }

    /**
     * @return an affine transform between two coordinate systems. Only units and axis order (e.g. transforming from
     *         (NORTH,WEST) to (EAST,NORTH)) are taken in account. Other attributes (especially the datum) must be
     *         checked before invoking this method.
     * 
     * @param sourceCRS
     *            The source coordinate system.
     * @param targetCRS
     *            The target coordinate system.
     * @throws TransformationException
     *             if some error occurs.
     */
    public static Matrix swapAxis( final CoordinateSystem sourceCRS, final CoordinateSystem targetCRS )
                            throws TransformationException {
        final Matrix matrix;
        try {
            matrix = new Matrix( sourceCRS.getAxis(), targetCRS.getAxis() );
        } catch ( RuntimeException e ) {
            throw new TransformationException( sourceCRS, targetCRS, e.getMessage() );
        }
        return matrix.isIdentity() ? null : matrix;
    }

}
