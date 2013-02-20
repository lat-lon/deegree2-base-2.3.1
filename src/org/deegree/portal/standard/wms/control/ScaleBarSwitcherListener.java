//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/wms/control/ScaleBarSwitcherListener.java $
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
package org.deegree.portal.standard.wms.control;

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
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.portal.Constants;

/**
 * The ScaleSwitcherListner handles switching the scalebar values from igeoportal
 * 
 * @author <a href="mailto:ncho@lat-lon.de">Serge N'Cho</a>
 * @author last edited by: $$Author: rbezema $$
 * 
 * @version $$Revision: 9458 $$, $$Date: 2008-01-08 12:04:46 +0100 (Tue, 08 Jan 2008) $$
 */

public class ScaleBarSwitcherListener extends AbstractMapListener {

    private static final ILogger LOG = LoggerFactory.getLogger( ScaleBarSwitcherListener.class );

    /**
     * Comment for "unit"
     */
    public static final String UNIT = "unit";

    /**
     * Comment for "taskFromListener"
     */
    public static final String TASK_FROM_LISTENER = "taskFromListener";

    /**
     * Comment for "scaleBarValue"
     */
    public static final String SCALE_BAR_VALUE = "scaleBarValue";

    /**
     * Comment for "newScaleBarValue"
     */
    public static final String NEW_SCALE_BAR_VALUE = "newScaleBarValue";

    /**
     * Comment for "BBOX"
     */
    public static final String BBOX = "BBOX";

    /**
     * Comment for "newBBox"
     */
    public static final String NEW_BBOX = "newBBox";

    /**
     * Constant for "crs"
     */
    private static final String CRS = "crs";

    /**
     * Constant for "taskFromJSObject"
     */
    private static final String JS_TAK = "taskFromJSObject";

    /**
     * Constant for "requestedBarValue"
     */
    private static final String REQUESTED_BAR_VALUE = "requestedBarValue";

    /**
     * Constant for "getNewBBOX"
     */
    private static final String GET_NEW_BBOX = "getNewBBOX";

    /**
     * Constant for "getActualScaleBarValue"
     */
    private static final String GET_ACTUAL_BAR_VAULE = "getActualScaleBarValue";

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.enterprise
     *      .control.WebListener#actionPerformed(org.deegree.enterprise.control.FormEvent)
     */
    public void actionPerformed( FormEvent event ) {

        

        RPCWebEvent rpc = (RPCWebEvent) event;
        RPCMethodCall mc = rpc.getRPCMethodCall();
        RPCParameter param = mc.getParameters()[0];
        RPCStruct struct = (RPCStruct) param.getValue();
        double minx = (Double) struct.getMember( Constants.RPC_BBOXMINX ).getValue();
        double miny = (Double) struct.getMember( Constants.RPC_BBOXMINY ).getValue();
        double maxx = (Double) struct.getMember( Constants.RPC_BBOXMAXX ).getValue();
        double maxy = (Double) struct.getMember( Constants.RPC_BBOXMAXY ).getValue();
        String crsString = struct.getMember( CRS ).getValue().toString();
        CoordinateSystem crs;
        try {
            crs = CRSFactory.create( crsString );
        } catch ( UnknownCRSException e ) {
            gotoErrorPage( e.getMessage() );
            return;
        }
        Envelope inBBOX = GeometryFactory.createEnvelope( minx, miny, maxx, maxy, crs );
        String unit = struct.getMember( UNIT ).getValue().toString();
        String taskFromJSObject = struct.getMember( JS_TAK ).getValue().toString();
        double actualBarValue = calculateScaleBarValue( inBBOX );

        if ( !"m".equalsIgnoreCase( unit ) ) {
            actualBarValue = convertToUnit( actualBarValue, unit );
        }

        ServletRequest request = this.getRequest();

        if ( GET_ACTUAL_BAR_VAULE.equalsIgnoreCase( taskFromJSObject ) ) {
            request.setAttribute( TASK_FROM_LISTENER, NEW_SCALE_BAR_VALUE );
            request.setAttribute( SCALE_BAR_VALUE, Double.valueOf( actualBarValue ) );
        } else if ( GET_NEW_BBOX.equals( taskFromJSObject ) ) {

            Double requestedBarValue = (Double) struct.getMember( REQUESTED_BAR_VALUE ).getValue();

            Envelope newBBox = MapUtils.scaleEnvelope( inBBOX, actualBarValue, requestedBarValue );

            double[] bbox = { newBBox.getMin().getX(), newBBox.getMin().getY(),
                             newBBox.getMax().getX(), newBBox.getMax().getY() };

            request.setAttribute( TASK_FROM_LISTENER, NEW_BBOX );
            request.setAttribute( BBOX, bbox );
            request.setAttribute( SCALE_BAR_VALUE, requestedBarValue );

        }

        request.setAttribute( UNIT, unit );
        
    }

    /**
     * The methode <code>calculateScaleBarValue</code> calculates the scaleBarValue of the Map.
     * 
     * @param bbox
     *            the actual Envelope of the map
     * @param mapWidth
     * @param mapHeight
     * @return - The distance from <code>bbox</code>.minx to <code>.maxx in meters
     */
    private double calculateScaleBarValue( Envelope bbox  ) {

        double scaleBarValue = 0;

        try {
            // Convert BBox to EPSG:4326 bevor calculating the scalBarValue
            if ( !"EPSG:4326".equalsIgnoreCase( bbox.getCoordinateSystem().getName() ) ) {

                GeoTransformer transformer = new GeoTransformer( "EPSG:4326" );
                bbox = transformer.transform( bbox, bbox.getCoordinateSystem() );

            }

            scaleBarValue = MapUtils.calcDistance( bbox.getMin().getX(), bbox.getMin().getY(),
                                                  bbox.getMax().getX(), bbox.getMin().getY() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return scaleBarValue;
    }

    /**
     * The methode converts the length -actualValue- to unit
     * 
     * @param actualValue -
     *            The length to be converted
     * @param unit -
     *            The unit in which the length is to converted to
     * @return - The converted length to unit
     */
    private double convertToUnit( double actualValue, String unit ) {

        if ( "km".equalsIgnoreCase( unit ) ) {
            return actualValue * 0.001;
        }
        // FIXME to which unit should this be extended to

        return actualValue;
    }

}