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

import javax.vecmath.Point3d;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.model.crs.CRSTransformationException;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.GeoTransformer;

/**
 * <code>SimpleData</code> takes a list of points which will be transformed.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class SimpleData extends TransformableData<Point3d> {
    private static ILogger LOG = LoggerFactory.getLogger( SimpleData.class );
    
    /**
     * The coordinate separator, default to ','
     */
    private final String cs;
    
    /**
     * The tuple separator, default to ' '
     */
    private final String ts;

    private List<Point3d> sourcePoints;

    /**
     * Creates a simple data instance.
     * 
     * @param sourceCRS
     *            in which the data is referenced.
     * @param targetCRS
     *            to which the data should be transformed.
     * @param transformableData
     * @param cs the coordinate separator
     * @param ts the tuple separator
     * @throws IllegalArgumentException
     *             if either one of the crs's are <code>null</code>.
     */
    public SimpleData( CoordinateSystem sourceCRS, CoordinateSystem targetCRS, List<Point3d> transformableData, String cs, String ts )
                            throws IllegalArgumentException {
        super( sourceCRS, targetCRS );
        if ( transformableData == null ) {
            transformableData = new ArrayList<Point3d>();
        }
        this.sourcePoints = transformableData;
        if( cs == null || "".equals( cs ) ){
            cs = ",";
        }
        this.cs = cs;

        if( ts == null || "".equals( ts ) ){
            ts = " ";
        }
        this.ts = ts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.wcts.operation.TransformableData#doTransform(org.deegree.model.crs.CoordinateSystem,
     *      org.deegree.model.crs.CoordinateSystem, boolean)
     */
    @Override
    public void doTransform( boolean enableLogging ) {
        GeoTransformer transformer = getGeotransformer();
        try {
            sourcePoints = transformer.transform( getSourceCRS(), sourcePoints );
        } catch ( IllegalArgumentException e ) {
            LOG.logError( e.getMessage(), e );
        } catch ( CRSTransformationException e ) {
            LOG.logError( e.getMessage(), e );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.wcts.operation.TransformableData#getResult()
     */
    @Override
    public List<Point3d> getTransformedData() {
        return sourcePoints;
    }

    /**
     * @return the value for the ts attribute, defaults to a single space character.
     */
    public final String getTupleSeparator() {
        return ts;
    }
    
    /**
     * @return the value for the cs attribute, defaults to a single comma character.
     */
    public final String getCoordinateSeparator() {
        return cs;
    }

}
