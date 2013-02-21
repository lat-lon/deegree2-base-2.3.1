//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/MapUtils.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53115 Bonn
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
package org.deegree.framework.util;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Position;

/**
 * 
 * 
 * @version $Revision: 11912 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 11912 $, $Date: 2008-05-27 12:02:29 +0200 (Di, 27. Mai 2008) $
 * 
 * @since 2.0
 */
public class MapUtils {

    private static ILogger LOG = LoggerFactory.getLogger( MapUtils.class );

    /**
     * The Value of sqrt(2)
     */
    public static final double SQRT2 = 1.4142135623730950488016887242096980785;

    /**
     * The Value of a PixelSize
     */
    public static final double DEFAULT_PIXEL_SIZE = 0.00028;

    /**
     * calculates the map scale (denominator) as defined in the OGC SLD 1.0.0 specification
     * 
     * @param mapWidth
     *            map width in pixel
     * @param mapHeight
     *            map height in pixel
     * @param bbox
     *            bounding box of the map
     * @param crs
     *            coordinate reference system of the map
     * @param pixelSize
     *            size of one pixel of the map measured in meter
     * 
     * @return a maps scale based on the diagonal size of a pixel at the center of the map in meter.
     * @throws RuntimeException
     */
    public static double calcScale( int mapWidth, int mapHeight, Envelope bbox, CoordinateSystem crs, double pixelSize )
                            throws RuntimeException {

        if ( mapWidth == 0 || mapHeight == 0 ) {
            return 0;
        }

        double scale = 0;

        CoordinateSystem cs = crs;

        if ( cs == null ) {
            throw new RuntimeException( "Invalid crs: " + crs );
        }

        try {
            if ( "m".equalsIgnoreCase( cs.getAxisUnits()[0].toString() ) ) {
                /*
                 * this method to calculate a maps scale as defined in OGC WMS and SLD specification
                 * is not required for maps having a projected reference system. Direct calculation
                 * of scale avoids uncertainties
                 */
                double bboxWidth = bbox.getWidth();
                double bboxHeight = bbox.getHeight();
                double d1 = Math.sqrt( ( mapWidth * mapWidth ) + ( mapHeight * mapHeight ) );
                double d2 = Math.sqrt( ( bboxWidth * bboxWidth ) + ( bboxHeight * bboxHeight ) );
                scale = ( d2 / d1 ) / pixelSize;
            } else {

                if ( !crs.getIdentifier().equalsIgnoreCase( "EPSG:4326" ) ) {
                    // transform the bounding box of the request to EPSG:4326
                    GeoTransformer trans = new GeoTransformer( CRSFactory.create( "EPSG:4326" ) );
                    bbox = trans.transform( bbox, crs );
                }
                double dx = bbox.getWidth() / mapWidth;
                double dy = bbox.getHeight() / mapHeight;
                Position min = GeometryFactory.createPosition( bbox.getMin().getX() + dx * ( mapWidth / 2d - 1 ),
                                                               bbox.getMin().getY() + dy * ( mapHeight / 2d - 1 ) );
                Position max = GeometryFactory.createPosition( bbox.getMin().getX() + dx * ( mapWidth / 2d ),
                                                               bbox.getMin().getY() + dy * ( mapHeight / 2d ) );

                double distance = calcDistance( min.getX(), min.getY(), max.getX(), max.getY() );

                scale = distance * ( SQRT2 / pixelSize );

            }
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new RuntimeException( Messages.getMessage( "FRAMEWORK_ERROR_SCALE_CALC", e.getMessage() ) );
        }

        return scale;

    }

    /**
     * calculates the distance in meters between two points in EPSG:4326 coodinates. this is a
     * convenience method assuming the world is a ball
     * 
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return the distance in meters between two points in EPSG:4326 coords
     */
    public static double calcDistance( double lon1, double lat1, double lon2, double lat2 ) {
        double r = 6378.137;
        double rad = Math.PI / 180d;
        double cose = Math.sin( rad * lon1 ) * Math.sin( rad * lon2 ) + Math.cos( rad * lon1 ) * Math.cos( rad * lon2 )
                      * Math.cos( rad * ( lat1 - lat2 ) );
        double dist = r * Math.acos( cose ) * Math.cos( rad * Math.min( lat1, lat2 ) );

        // * 0.75 is just an heuristic correction factor
        return dist * 1000 * 0.75;
    }

    /**
     * The method calculates a new Envelope from the <code>requestedBarValue</code> It will either
     * zoom in or zoom out of the <code>actualBBOX<code> depending
     * on the ratio of the <code>requestedBarValue</code> to the <code>actualBarValue</code>
     * @param currentEnvelope current Envelope 
     * @param currentScale the scale of the current envelope
     * @param requestedScale requested scale value 
     * @return a new Envelope
     */
    public static Envelope scaleEnvelope( Envelope currentEnvelope, double currentScale, double requestedScale ) {

        double ratio = requestedScale / currentScale;
        double newWidth = currentEnvelope.getWidth() * ratio;
        double newHeight = currentEnvelope.getHeight() * ratio;
        double midX = currentEnvelope.getMin().getX() + ( currentEnvelope.getWidth() / 2d );
        double midY = currentEnvelope.getMin().getY() + ( currentEnvelope.getHeight() / 2d );

        double minx = midX - newWidth / 2d;
        double maxx = midX + newWidth / 2d;
        double miny = midY - newHeight / 2d;
        double maxy = midY + newHeight / 2d;

        return GeometryFactory.createEnvelope( minx, miny, maxx, maxy, currentEnvelope.getCoordinateSystem() );

    }

    /**
     * This method ensures the bbox is resized (shrunk) to match the aspect ratio defined by
     * mapHeight/mapWidth
     * 
     * @param bbox
     * @param mapWith
     * @param mapHeight
     * @return a new bounding box with the aspect ratio given my mapHeight/mapWidth
     */
    public static final Envelope ensureAspectRatio( Envelope bbox, double mapWith, double mapHeight ) {

        double minx = bbox.getMin().getX();
        double miny = bbox.getMin().getY();
        double maxx = bbox.getMax().getX();
        double maxy = bbox.getMax().getY();

        double dx = maxx - minx;
        double dy = maxy - miny;

        double ratio = mapHeight / mapWith;

        if ( dx >= dy ) {
            // height has to be corrected
            double[] normCoords = getNormalizedCoords( dx, ratio, miny, maxy );
            miny = normCoords[0];
            maxy = normCoords[1];
        } else {
            // width has to be corrected
            ratio = mapWith / mapHeight;
            double[] normCoords = getNormalizedCoords( dy, ratio, minx, maxx );
            minx = normCoords[0];
            maxx = normCoords[1];
        }
        CoordinateSystem crs = bbox.getCoordinateSystem();

        return GeometryFactory.createEnvelope( minx, miny, maxx, maxy, crs );
    }

    private static final double[] getNormalizedCoords( double normLen, double ratio, double min, double max ) {
        double mid = ( max - min ) / 2 + min;
        min = mid - ( normLen / 2 ) * ratio;
        max = mid + ( normLen / 2 ) * ratio;
        double[] newCoords = { min, max };
        return newCoords;
    }

}
