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

import java.util.ArrayList;
import java.util.List;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.model.crs.CRSTransformationException;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wcts.WCTSExceptionCode;

/**
 * <code>GeometryData</code> encapsulates a list of geometries which can be transformed using the
 * {@link #doTransform(boolean)} method.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class GeometryData extends TransformableData<Geometry> {
    private List<Geometry> sourceGeometries;

    private final List<Geometry> transformedGeometries;

    private static ILogger LOG = LoggerFactory.getLogger( GeometryData.class );

    /**
     * Creates a data instance which handles geometries.
     * 
     * @param sourceCRS
     *            in which the data is referenced.
     * @param targetCRS
     *            to which the data should be transformed.
     * @param transformableData
     *            to transform
     * @throws IllegalArgumentException
     *             if either one of the crs's are <code>null</code>.
     */
    public GeometryData( CoordinateSystem sourceCRS, CoordinateSystem targetCRS, List<Geometry> transformableData ) throws IllegalArgumentException{
        super( sourceCRS, targetCRS );
        if ( transformableData == null ) {
            transformableData = new ArrayList<Geometry>();
        }
        this.sourceGeometries = transformableData;
        transformedGeometries = new ArrayList<Geometry>( this.sourceGeometries.size() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.wcts.operation.TransformableData#doTransform(boolean)
     */
    @Override
    public void doTransform( boolean enableLogging ) throws OGCWebServiceException {
        GeoTransformer transformer = getGeotransformer();
        for ( Geometry geom : sourceGeometries ) {
            try {
                if( !getSourceCRS().equals( geom.getCoordinateSystem() ) ){
                    throw new OGCWebServiceException( Messages.getMessage( "WCTS_MISMATCHING_CRS_DEFINITIONS" , getSourceCRS().getIdentifier(), geom.getCoordinateSystem().getIdentifier() ), WCTSExceptionCode.INVALIDPARAMETERVALUE );
                }
                transformedGeometries.add( transformer.transform( geom ) );
            } catch ( IllegalArgumentException e ) {
                LOG.logError( e.getMessage(), e );
            } catch ( CRSTransformationException e ) {
                LOG.logError( e.getMessage(), e );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.wcts.operation.TransformableData#getResult()
     */
    @Override
    public List<Geometry> getTransformedData() {
        return transformedGeometries;
    }

}
