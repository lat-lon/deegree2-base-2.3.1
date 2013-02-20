//$HeadURL: svn+ssh://melmasry@svn.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/context/control/ContextSaveListener.java $
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

 Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: klaus.greve@uni-bonn.de
 
 ---------------------------------------------------------------------------*/

package org.deegree.portal.standard.wms.control;

import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.deegree.enterprise.control.RPCMember;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCParameter;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.StringTools;
import org.deegree.i18n.Messages;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Point;
import org.deegree.ogcwebservices.OWSUtils;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilitiesDocument;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilitiesDocumentFactory;
import org.deegree.portal.Constants;
import org.deegree.portal.PortalException;
import org.deegree.portal.common.control.AbstractSimplePrintListener;
import org.deegree.portal.context.ContextException;
import org.deegree.portal.context.General;
import org.deegree.portal.context.Layer;
import org.deegree.portal.context.LayerList;
import org.deegree.portal.context.Server;
import org.deegree.portal.context.ViewContext;
import org.deegree.portal.standard.context.control.ContextSaveListener;

/**
 * This class prints the View context. It inherits from AbstractSimplePrintListner and implement the abstract method
 * getViewContext
 * 
 * TODO The methods changeBBox(), changeLayerList(), extractBBox() are already implemented in AbstractContextListner.
 * The question now is wether to inherit from AbstractContextListner instead of AbstractListner?
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: mays $
 * 
 * @version $Revision: 1.2 $, $Date: 2007-10-04 13:31:39 $
 */
public class SimplePrintListener extends AbstractSimplePrintListener {

    private static final ILogger LOG = LoggerFactory.getLogger( ContextSaveListener.class );

    @Override
    protected ViewContext getViewContext( RPCWebEvent rpc ) {

        RPCMethodCall mc = rpc.getRPCMethodCall();
        RPCParameter[] params = mc.getParameters();
        RPCStruct struct = (RPCStruct) params[2].getValue();

        HttpSession session = ( (HttpServletRequest) getRequest() ).getSession();
        ViewContext vc = (ViewContext) session.getAttribute( Constants.CURRENTMAPCONTEXT );
        // change values: BBOX and Layer List
        Envelope bbox = extractBBox( (RPCStruct) struct.getMember( "boundingBox" ).getValue(), null );
        RPCMember[] layerList = ( (RPCStruct) struct.getMember( "layerList" ).getValue() ).getMembers();
        try {
            changeBBox( vc, bbox );
            changeLayerList( vc, layerList );
        } catch ( PortalException e ) {
            LOG.logError( "An Error occured while trying to get the Viewcontext" );
            return null;
            // TODO This method implements its abstract method in AbstractSimplePrintListner
            // Preferably if the abstract header could be changed to throw a adequate excpetion
            // since this header is already implemented by another class
        }

        return vc;
    }

    /**
     * Convenience method to extract the boundig box from an rpc fragment.
     * 
     * @param bboxStruct
     *            the <code>RPCStruct</code> containing the bounding box. For example,
     *            <code>&lt;member&gt;&lt;name&gt;boundingBox&lt;/name&gt;etc...</code>.
     * @param crs
     *            a coordinate system value, may be null.
     * @return an envelope with the boundaries defined in the rpc structure
     */
    protected Envelope extractBBox( RPCStruct bboxStruct, CoordinateSystem crs ) {

        Double minx = (Double) bboxStruct.getMember( Constants.RPC_BBOXMINX ).getValue();
        Double miny = (Double) bboxStruct.getMember( Constants.RPC_BBOXMINY ).getValue();
        Double maxx = (Double) bboxStruct.getMember( Constants.RPC_BBOXMAXX ).getValue();
        Double maxy = (Double) bboxStruct.getMember( Constants.RPC_BBOXMAXY ).getValue();

        Envelope bbox = GeometryFactory.createEnvelope( minx.doubleValue(), miny.doubleValue(), maxx.doubleValue(),
                                                        maxy.doubleValue(), crs );
        return bbox;
    }

    /**
     * changes the layer list of the ViewContext vc according to the information contained in the rpcLayerList
     * 
     * @param vc
     *            The original ViewContext where the changes will be applied to
     * @param rpcLayerList
     *            the current layerlist
     * @throws PortalException
     */
    protected void changeLayerList( ViewContext vc, RPCMember[] rpcLayerList )
                            throws PortalException {
        LayerList layerList = vc.getLayerList();
        ArrayList<Layer> nLayers = new ArrayList<Layer>( rpcLayerList.length );

        // this is needed to keep layer order
        // order is correct in rpc call JavaScript, but get lost in translation...
        for ( int i = 0; i < rpcLayerList.length; i++ ) {
            String[] v = StringTools.toArray( (String) rpcLayerList[i].getValue(), "|", false );
            String n = rpcLayerList[i].getName();

            String title = n;
            if ( v.length > 5 ) {
                // this check is necessary, in order to not break running iGeoPortal instances
                title = v[5];
            }
            boolean isQueryable = false;
            if ( v.length > 6 ) {
                // JM: this check is necessary, in order to not break running iGeoPortal instances
                isQueryable = v[6].equalsIgnoreCase( "true" );
            }

            boolean isVisible = Boolean.valueOf( v[0] ).booleanValue();
            Layer l = layerList.getLayer( n, null );
            if ( l != null ) {
                // needed to reconstruct new layer order
                // otherwise layer order is still from original context
                l.setHidden( !isVisible );
            } else {

                if ( layerList.getLayers().length == 0 ) {
                    // FIXME is this Exception Correct
                    throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_EMPTY_LAYERLIST" ) );
                }

                Layer p = layerList.getLayers()[0];
                // a new layer must be created because it is not prsent in the current context. 
                // This is the case if the client has loaded an additional WMS
                String[] tmp = StringTools.toArray( v[2], " ", false );
                try {
                    v[4] = OWSUtils.validateHTTPGetBaseURL( v[4] );
                    WMSCapabilitiesDocument doc = 
                        WMSCapabilitiesDocumentFactory.getWMSCapabilitiesDocument( new URL( v[4] + "request=GetCapabilities&service=WMS" ) );
                    OGCCapabilities capa = doc.parseCapabilities();
                    Server server = new Server( v[3], tmp[1], tmp[0], new URL( v[4] ), capa );
                    l = new Layer( server, n, title, "", p.getSrs(), null, null, p.getFormatList(), p.getStyleList(),
                                   isQueryable, !isVisible, null );
                } catch ( Exception e ) {
                    throw new PortalException( StringTools.stackTraceToString( e ) );
                }
            }
            nLayers.add( l );
        }
        try {
            nLayers.trimToSize();
            Layer[] ls = new Layer[nLayers.size()];
            ls = nLayers.toArray( ls );
            vc.setLayerList( new LayerList( ls ) );
        } catch ( ContextException e ) {
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_SET_LAYERLIST",
                                                            StringTools.stackTraceToString( e.getStackTrace() ) ) );
        }
    }

    /**
     * changes the bounding box of a given view context
     * 
     * @param vc
     *            the view context to be changed
     * @param bbox
     *            the new bounding box
     * @throws PortalException
     */
    public static final void changeBBox( ViewContext vc, Envelope bbox )
                            throws PortalException {
        General gen = vc.getGeneral();

        CoordinateSystem cs = gen.getBoundingBox()[0].getCoordinateSystem();
        Point[] p = new Point[] { GeometryFactory.createPoint( bbox.getMin(), cs ),
                                  GeometryFactory.createPoint( bbox.getMax(), cs ) };
        try {
            gen.setBoundingBox( p );
        } catch ( ContextException e ) {
            LOG.logError( e.getMessage(), e );
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_SET_BBOX" ) );
        }
    }

}
