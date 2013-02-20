//$HeadURL: $
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

package org.deegree.ogcwebservices.wcts.data;

import java.util.List;

import javax.vecmath.Point3d;

import org.deegree.i18n.Messages;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.ogcwebservices.OGCWebServiceException;

/**
 * <code>TransformableData</code> defines the interface for different kinds of Data, which can be transformed.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * @param <T>
 *            the type of data.
 * 
 */
public abstract class TransformableData<T> {

    private final CoordinateSystem targetCRS;

    private final CoordinateSystem sourceCRS;

    /**
     * The parent of all transformable data.
     * 
     * @param sourceCRS
     *            in which the data is referenced.
     * @param targetCRS
     *            to which the data should be transformed.
     * @throws IllegalArgumentException
     *             if either one of the crs's are <code>null</code>.
     */
    public TransformableData( CoordinateSystem sourceCRS, CoordinateSystem targetCRS ) throws IllegalArgumentException {
        if ( sourceCRS == null ) {
            throw new IllegalArgumentException( Messages.getMessage( "WCTS_MISSING_ARGUMENT", "sourceCRS" ) );
        }
        if ( targetCRS == null ) {
            throw new IllegalArgumentException( Messages.getMessage( "WCTS_MISSING_ARGUMENT", "targetCRS" ) );

        }
        this.sourceCRS = sourceCRS;
        this.targetCRS = targetCRS;
    }

    /**
     * This function should implement the transforming of the underlying data.
     * 
     * @param enableLogging
     *            if true the implementing class should log all transformations.
     * @throws OGCWebServiceException if an exception occurs while transforming.
     */
    public abstract void doTransform( boolean enableLogging ) throws OGCWebServiceException;

    /**
     * @return the targetCRS.
     */
    public final CoordinateSystem getTargetCRS() {
        return targetCRS;
    }

    /**
     * @return the sourceCRS.
     */
    public final CoordinateSystem getSourceCRS() {
        return sourceCRS;
    }

    /**
     * @return a new GeoTransformer instance initialized with the targetCRS.
     */
    public final GeoTransformer getGeotransformer() {
        return new GeoTransformer( targetCRS );
    }

    /**
     * @return the transformed data as a list of the overriding class implementation, for example {@link Geometry}
     *         (GeometyData), or {@link Point3d} (SimpleData).
     */
    public abstract List<T> getTransformedData();

}
