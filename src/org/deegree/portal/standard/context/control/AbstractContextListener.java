//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/context/control/AbstractContextListener.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 University of Bonn
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
 53177 Bonn
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
package org.deegree.portal.standard.context.control;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.deegree.enterprise.WebUtils;
import org.deegree.enterprise.control.AbstractListener;
import org.deegree.enterprise.control.RPCMember;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCParameter;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCUtils;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.framework.xml.XSLTDocument;
import org.deegree.i18n.Messages;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Point;
import org.deegree.ogcbase.BaseURL;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.OWSUtils;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilitiesDocument;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilitiesDocumentFactory;
import org.deegree.portal.Constants;
import org.deegree.portal.PortalException;
import org.deegree.portal.context.ContextException;
import org.deegree.portal.context.General;
import org.deegree.portal.context.GeneralExtension;
import org.deegree.portal.context.Layer;
import org.deegree.portal.context.LayerList;
import org.deegree.portal.context.Server;
import org.deegree.portal.context.ViewContext;
import org.xml.sax.SAXException;

/**
 * This exception shall be thrown when a session(ID) will be used that has been expired.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
abstract public class AbstractContextListener extends AbstractListener {

    private static ILogger LOG = LoggerFactory.getLogger( AbstractContextListener.class );

    private static final NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    /**
     * gets the user name assigned to the passed session ID from an authentication service. If no user is assigned to
     * the session ID <tt>null</tt> will be returned. If the session is closed or expired an exception will be thrown
     * 
     * @param sessionId
     * @return name of the user assigned to the passed session ID
     * @throws XMLParsingException
     * @throws SAXException
     * @throws IOException
     */
    protected String getUserName( String sessionId )
                            throws XMLParsingException, IOException, SAXException {

        HttpSession session = ( (HttpServletRequest) getRequest() ).getSession( true );
        ViewContext vc = (ViewContext) session.getAttribute( Constants.CURRENTMAPCONTEXT );
        if ( vc == null ) {
            return null;
        }
        GeneralExtension ge = vc.getGeneral().getExtension();
        String userName = null;
        if ( sessionId != null && ge.getAuthentificationSettings() != null ) {
            LOG.logDebug( "try getting user from WAS/sessionID" );
            BaseURL baseUrl = ge.getAuthentificationSettings().getAuthentificationURL();
            String url = OWSUtils.validateHTTPGetBaseURL( baseUrl.getOnlineResource().toExternalForm() );
            StringBuffer sb = new StringBuffer( url );
            sb.append( "request=DescribeUser&SESSIONID=" ).append( sessionId );

            XMLFragment xml = new XMLFragment();
            xml.load( new URL( sb.toString() ) );

            userName = XMLTools.getRequiredNodeAsString( xml.getRootElement(), "/User/UserName", nsContext );
        } else {
            LOG.logDebug( "try getting user from getUserPrincipal()" );
            if ( ( (HttpServletRequest) getRequest() ).getUserPrincipal() != null ) {
                userName = ( (HttpServletRequest) getRequest() ).getUserPrincipal().getName();
                if ( userName.indexOf( "\\" ) > 1 ) {
                    String[] us = StringTools.toArray( userName, "\\", false );
                    userName = us[us.length - 1];
                }
            }
        }
        LOG.logDebug( "userName: " + userName );
        return userName;
    }

    /**
     * reads the users session ID.<br>
     * first the PRC will be parsed for a 'sessionID' element. If not present the sessionID will be read from the users
     * session. If even the user's HTTP session does not contain a sessionID, it will be tried to get it from the WAS
     * registered to the current context. If no WAS available <code>null</code> will be returned.
     * 
     * @param struct
     * @return the users session id
     * @throws IOException
     * @throws SAXException
     * @throws XMLParsingException
     */
    protected String readSessionID( RPCStruct struct )
                            throws XMLParsingException, SAXException, IOException {
        String sid = RPCUtils.getRpcPropertyAsString( struct, "sessionID" );
        if ( sid == null ) {
            LOG.logDebug( "try getting sessionID from HTTP session" );
            HttpSession session = ( (HttpServletRequest) getRequest() ).getSession();
            sid = (String) session.getAttribute( "SESSIONID" );
        }
        if ( sid == null ) {
            // try get SessionID from WAS if user name is available
            // in this case it is assumed that a user's name can be determined
            // evaluating the requests userPrincipal that will be available if
            // the user has been logged in to the the server (or network)
            String userName = getUserName( null );
            if ( userName != null ) {
                LOG.logDebug( "try getting sessionID by authorizing current user: " + userName );
                HttpSession session = ( (HttpServletRequest) getRequest() ).getSession( true );
                ViewContext vc = (ViewContext) session.getAttribute( Constants.CURRENTMAPCONTEXT );
                GeneralExtension ge = vc.getGeneral().getExtension();
                if ( ge.getAuthentificationSettings() != null ) {
                    BaseURL baseUrl = ge.getAuthentificationSettings().getAuthentificationURL();
                    StringBuffer sb = new StringBuffer( 500 );
                    String addr = baseUrl.getOnlineResource().toExternalForm();
                    sb.append( OWSUtils.validateHTTPGetBaseURL( addr ) );
                    sb.append( "SERVICE=WAS&VERSION=1.0.0&REQUEST=GetSession&" );
                    sb.append( "AUTHMETHOD=urn:x-gdi-nrw:authnMethod:1.0:password&CREDENTIALS=" );
                    sb.append( userName );
                    LOG.logDebug( "authenticat user: ", sb.toString() );
                    HttpClient client = new HttpClient();
                    client = WebUtils.enableProxyUsage( client, baseUrl.getOnlineResource() );
                    GetMethod meth = new GetMethod( sb.toString() );
                    client.executeMethod( meth );
                    sid = meth.getResponseBodyAsString();
                    session.setAttribute( "SESSIONID", sid );
                }
            }
        }
        LOG.logDebug( "sessionID: " + sid );
        return sid;
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
     * This method is kept for downward compatibility of the API. Do not use it any more!
     *  
     * @deprecated use extractBBox( RPCStruct, CoordinateSystem ) instead.
     * @param bboxStruct
     *            the <code>RPCStruct</code> containing the bounding box. For example,
     *            <code>&lt;member&gt;&lt;name&gt;boundingBox&lt;/name&gt;etc...</code>.
     * @return an envelope with the boundaries defined in the rpc structure
     */
    @Deprecated
    protected Envelope extractBBox( RPCStruct bboxStruct ) {
        return extractBBox( bboxStruct, null );
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
        // order is correct in rpc call JavaScript) but get lost in translation...
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
                // a new layer must be created because it is not prsent
                // in the current context. This is the case if the client
                // has loaded an additional WMS
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
     * This function takes in a XmlFragment and transforms it to a html map context
     * 
     * @param xml
     *            xmlFragment to transform
     * @param xsl
     *            xsl file used to transform
     * @return html representing the mapContext
     * @throws TransformerException
     * @throws SAXException
     * @throws IOException
     * @throws MalformedURLException
     */
    protected String transformToHtmlMapContext( XMLFragment xml, String xsl )
                            throws TransformerException, MalformedURLException, IOException, SAXException {

        XSLTDocument xslt = new XSLTDocument();
        StringWriter sw = new StringWriter( 30000 );

        xslt.load( new URL( xsl ) );
        xml = xslt.transform( xml );
        xml.write( sw );

        return sw.toString();
    }

    /**
     * Extracts the parameters from the method call element within the passed rpcEvent.
     * 
     * @param rpcEvent
     * @return Returns the parameters as array of <code>RPCParameter</code>.
     * @throws PortalException
     */
    protected RPCParameter[] extractRPCParameters( RPCWebEvent rpcEvent )
                            throws PortalException {
        RPCParameter[] params;
        try {
            RPCMethodCall mc = rpcEvent.getRPCMethodCall();
            params = mc.getParameters();
        } catch ( Exception e ) {
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_EXTRACT_PARAM_RPC",
                                                            e.getMessage() ) );
        }
        return params;
    }
    
    /**
     * Extracts the <code>RPCStruct</code> from the indicated parameter in the params element of the passed
     * <code>RPCWebEvent</code>.
     * 
     * @param rpcEvent
     *            The RPCWebEvent, that contains the RPCStruct to extract.
     * @param index
     *            The index of the parameter from which to extract the RPCStruct (starting with 0).
     * @return Returns the <code>RPCStruct</code> from the indicated params element.
     * @throws PortalException
     */
    protected RPCStruct extractRPCStruct( RPCWebEvent rpcEvent, int index )
                            throws PortalException {
        RPCStruct rpcStruct;
        try {
            RPCParameter[] params = extractRPCParameters( rpcEvent );
            rpcStruct = (RPCStruct) params[index].getValue();
        } catch ( Exception e ) {
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_EXTRACT_STRUCT_RPC",
                                                            e.getMessage() ) );
        }
        return rpcStruct;
    }

}
