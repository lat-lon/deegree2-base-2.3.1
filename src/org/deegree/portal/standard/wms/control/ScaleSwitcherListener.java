//$$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/wms/control/ScaleSwitcherListener.java $$
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
 Aennchenstraße 19
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

package org.deegree.portal.standard.wms.control;

import java.util.ResourceBundle;

import javax.servlet.ServletRequest;

import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCParameter;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.MapUtils;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.portal.Constants;
import org.deegree.portal.PortalException;

/**
 * The ScaleSwitcherListner handles switching the values of the scales from igeoportal
 * 
 * @author <a href="mailto:ncho@lat-lon.de">Serge N'Cho</a>
 * @author last edited by: $$Author: apoth $$
 * 
 * @version $$Revision: 9348 $$, $$Date: 2007-12-27 17:59:14 +0100 (Thu, 27 Dec 2007) $$
 */
public class ScaleSwitcherListener extends AbstractMapListener {

    private static final ILogger LOG = LoggerFactory.getLogger( ScaleSwitcherListener.class );

    private static double PIXEL_SIZE = 0.00028;

    static {
        final ResourceBundle bundle = ResourceBundle.getBundle( "org.deegree.portal.standard.wms.control.map_listener" ); //$NON-NLS-1$
        String ps = bundle.getString( "ScaleSwitcher.pixelSize" );
        if ( ps != null ) {
            PIXEL_SIZE = Double.valueOf( ps );
        }
    }

    /**
     * Constant for "taskFromListener"
     */
    public static final String TASK_FROM_LISTENER = "taskFromListener";

    /**
     * Constant for "BBOX"
     */
    public static final String BBOX = "BBOX";

    /**
     * Constant for "zoomToFullExtent"
     */
    public static final String FULL_EXTENT = "zoomToFullExtent";

    /**
     * Constant for "scaleValue"
     */
    public static final String SCALE_VALUE = "scaleValue";

    /**
     * Constant for "newScaleValue"
     */
    public static final String NEW_SCALE_VALUE = "newScaleValue";

    /**
     * Constant for "newBBox"
     */
    public static final String NEW_BBOX = "newBBox";

    /**
     * Constant for "crs"
     */
    private static final String CRS = "crs";

    /**
     * Constant for "mapWidth"
     */
    private static final String MAP_WIDTH = "mapWidth";

    /**
     * Constant for "mapHeight"
     */
    private static final String MAP_HEIGHT = "mapHeight";

    /**
     * Constant for "taskFromJSObject"
     */
    private static final String JS_TAK = "taskFromJSObject";

    /**
     * Constant for "requestedScale"
     */
    private static final String REQUESTED_SCALE = "requestedScale";

    /**
     * Constant for "getNewBBOX"
     */
    private static final String GET_NEW_BBOX = "getNewBBOX";

    /**
     * Constant for "savedScaleValue"
     */
    private static final String SAVED_SCALE = "savedScaleValue";

    /**
     * Constant for "getActualScaleValue"
     */
    private static final String GET_CURRENT_SCALE = "getActualScaleValue";

    /**
     * @see org.deegree.enterprise.control
     *      .WebListener#actionPerformed(org.deegree.enterprise.control.FormEvent)
     */
    @Override
    public void actionPerformed( FormEvent event ) {

        
        RPCWebEvent rpc = (RPCWebEvent) event;
        RPCMethodCall mc = rpc.getRPCMethodCall();
        RPCParameter param = mc.getParameters()[0];
        RPCStruct struct = (RPCStruct) param.getValue();
        double minx = (Double) struct.getMember( Constants.RPC_BBOXMINX ).getValue();
        double miny = (Double) struct.getMember( Constants.RPC_BBOXMINY ).getValue();
        double maxx = (Double) struct.getMember( Constants.RPC_BBOXMAXX ).getValue();
        double maxy = (Double) struct.getMember( Constants.RPC_BBOXMAXY ).getValue();
        String crs = struct.getMember( CRS ).getValue().toString();
        CoordinateSystem cs;
        try {
            cs = CRSFactory.create( crs );
        } catch ( UnknownCRSException e1 ) {
            gotoErrorPage( e1.getMessage() );
            return;
        }
        Envelope actualBBox = GeometryFactory.createEnvelope( minx, miny, maxx, maxy, cs );
        double mapWidth = (Double) struct.getMember( MAP_WIDTH ).getValue();
        double mapHeight = (Double) struct.getMember( MAP_HEIGHT ).getValue();
        String taskFromJSObject = struct.getMember( JS_TAK ).getValue().toString();
        ServletRequest request = this.getRequest();

        try {

            double currentScaleValue = MapUtils.calcScale( (int) mapWidth, (int) mapHeight,
                                                        actualBBox,
                                                        actualBBox.getCoordinateSystem(),
                                                        PIXEL_SIZE );

            if ( GET_NEW_BBOX.equals( taskFromJSObject ) ) {

                String scaleRequestString = struct.getMember( REQUESTED_SCALE ).getValue().toString();

                double requestedScale = Double.parseDouble( scaleRequestString.substring(
                                                                                          2,
                                                                                          scaleRequestString.length() ) );

                double savedScaleValue = (Double) struct.getMember( SAVED_SCALE ).getValue();
                Envelope newBbox = calcNewBBox( actualBBox, requestedScale, currentScaleValue,
                                                savedScaleValue );

                double[] bbox = { newBbox.getMin().getX(), newBbox.getMin().getY(),
                                 newBbox.getMax().getX(), newBbox.getMax().getY() };

                Integer scaleValue = new Integer( (int) ( requestedScale ) );
                request.setAttribute( SCALE_VALUE, scaleValue );
                request.setAttribute( BBOX, bbox );
                request.setAttribute( TASK_FROM_LISTENER, NEW_BBOX );

            } else if ( GET_CURRENT_SCALE.equals( taskFromJSObject ) ) {

                Integer scaleValue = new Integer( (int) currentScaleValue );
                request.setAttribute( SCALE_VALUE, scaleValue );
                request.setAttribute( TASK_FROM_LISTENER, NEW_SCALE_VALUE );

            } else {
                String message = "Unknown task from ScaleSwitcher module" + taskFromJSObject;
                throw new PortalException( message );
            }
        } catch ( PortalException e ) {
            request.setAttribute( TASK_FROM_LISTENER, FULL_EXTENT );

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        

    }

    /**
     * The methode <code>calcNewBBox</code> calculates a new boundingbox for a requested scale. It
     * will either zoom in or zoom out of the <code>actualBBOX<code> depending
     * on the ratio of the <code>requestedScale</code> to the <code>actualScale</code>
     * 
     * @param currentBBOX
     * @param requestedScale
     * @param currentScale
     * @param savedScaleValue
     * @return Envelope of the new BBox
     * @throws Exception
     */
    private Envelope calcNewBBox( Envelope currentBBOX, double requestedScale, double currentScale,
                                 double savedScaleValue )
                            throws Exception {

        
        Envelope newBBox = null;
        double ratio = requestedScale / currentScale;
        // NaN ration will return a null Envelope
        if ( Double.isNaN( ratio ) ) {
            // FIXME when does this occurs? and how should this be handle?
            throw new PortalException( "ratio is not a number" );
        }

        if ( Double.isInfinite( ratio ) ) {// infinite ratio will return infinite Envelope
            // the actualScale calculated was probably 0: use the saveScaleValue
            if ( savedScaleValue > 1 ) {
                ratio = requestedScale / savedScaleValue;
            }
            if ( Double.isInfinite( ratio ) ) {
                throw new PortalException( "ratio is infinite" );
            }
        }
        double newWidth = currentBBOX.getWidth() * ratio;
        double newHeight = currentBBOX.getHeight() * ratio;
        double midX = currentBBOX.getMin().getX() + ( currentBBOX.getWidth() / 2d );
        double midY = currentBBOX.getMin().getY() + ( currentBBOX.getHeight() / 2d );
        double minx = midX - newWidth / 2d;
        double maxx = midX + newWidth / 2d;
        double miny = midY - newHeight / 2d;
        double maxy = midY + newHeight / 2d;
        newBBox = GeometryFactory.createEnvelope( minx, miny, maxx, maxy,
                                                  currentBBOX.getCoordinateSystem() );
        
        return newBBox;
    }

}