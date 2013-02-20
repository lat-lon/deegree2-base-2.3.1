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
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wcts.WCTSExceptionCode;

/**
 * <code>FeatureCollectionData</code> encapsulates a list of FeatureCollections which can be transformed using the
 * {@link #doTransform(boolean)} method.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class FeatureCollectionData extends TransformableData<FeatureCollection> {

    private static ILogger LOG = LoggerFactory.getLogger( FeatureCollectionData.class );

    private List<FeatureCollection> sourceData;

    private List<FeatureCollection> transformedData;

    /**
     * Creates a data instance which handles feature collections.
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
    public FeatureCollectionData( CoordinateSystem sourceCRS, CoordinateSystem targetCRS,
                                  List<FeatureCollection> transformableData ) throws IllegalArgumentException {
        super( sourceCRS, targetCRS );
        if ( transformableData == null ) {
            transformableData = new ArrayList<FeatureCollection>();
        }
        this.sourceData = transformableData;
        this.transformedData = new ArrayList<FeatureCollection>( this.sourceData.size() );

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.wcts.operation.TransformableData#doTransform(boolean)
     */
    @Override
    public void doTransform( boolean enableLogging ) throws OGCWebServiceException {
        GeoTransformer transformer = getGeotransformer();
        CoordinateSystem sourceCRS = getSourceCRS();
        for ( FeatureCollection fc : sourceData ) {
            try {
                FeatureCollection result = transform( fc, transformer, sourceCRS );
                if ( result != null && result.size() != 0 ) {
                    transformedData.add( result );
                }
            } catch ( CRSTransformationException e ) {
                LOG.logError( e.getMessage(), e );
                throw new OGCWebServiceException( e.getMessage(), WCTSExceptionCode.NOAPPLICABLECODE );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.wcts.operation.TransformableData#getResult()
     */
    @Override
    public List<FeatureCollection> getTransformedData() {
        return transformedData;
    }

    /**
     * Transforms all geometries contained within the passed {@link FeatureCollection} into the given target CRS.
     * 
     * @param fc
     *            the collection to transform
     * @param transformer
     *            which will do the transforming
     * @param sourceCRS
     *            in which the data is referenced.
     * @return the transformed geometries in the FeatureCollection
     * @throws CRSTransformationException
     *             if the transformation cannot be created or processed.
     * @throws OGCWebServiceException if the crs of the geom does not match the requested crs
     */
    private FeatureCollection transform( FeatureCollection fc, GeoTransformer transformer, CoordinateSystem sourceCRS )
                                                                                                                       throws CRSTransformationException, OGCWebServiceException {
        for ( int i = 0; i < fc.size(); i++ ) {
            transform( fc.getFeature( i ), transformer, sourceCRS );
        }
        return fc;
    }

    /**
     * Sort of a copy of the {@link GeoTransformer#transform(Feature)} but is able to log everything per geometry.
     * Transforms all geometries contained within the passed {@link Feature} into the given target CRS. If a geometry
     * was transformed the {@link Feature#setEnvelopesUpdated()} method will be called.
     * 
     * @param feature
     * @param transformer
     *            which does the transforming.
     * @param sourceCRS
     *            to transform from
     * @return the transformed geometries in the given Feature.
     * @throws CRSTransformationException
     * @throws OGCWebServiceException if the crs of the geom does not match the requested crs
     */
    private Feature transform( Feature feature, GeoTransformer transformer, CoordinateSystem sourceCRS )
                                                                                                        throws CRSTransformationException, OGCWebServiceException {
        if ( feature != null ) {
            FeatureProperty[] featureProperties = feature.getProperties();
            if ( featureProperties != null ) {
                for ( FeatureProperty fp : featureProperties ) {
                    if ( fp != null ) {
                        Object value = fp.getValue();
                        if ( value != null ) {
                            if ( value instanceof Geometry ) {
                                Geometry geom = (Geometry) value;
                                if( !sourceCRS.equals( geom.getCoordinateSystem() ) ){
                                    throw new OGCWebServiceException( Messages.getMessage( "WCTS_MISMATCHING_CRS_DEFINITIONS" , sourceCRS.getIdentifier(), geom.getCoordinateSystem().getIdentifier()), WCTSExceptionCode.INVALIDPARAMETERVALUE );
                                }
                                fp.setValue( transformer.transform( geom ) );
                                feature.setEnvelopesUpdated();
                            } else if ( value instanceof Feature ) {
                                transform( (Feature) value, transformer, sourceCRS );
                            }
                        }
                    }
                }
            }
        }
        return feature;
    }

}
