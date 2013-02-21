//$$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/security/control/LogoutListener.java $$
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
package org.deegree.portal.standard.security.control;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.deegree.enterprise.control.AbstractListener;
import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.NetWorker;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.ogcbase.BaseURL;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.OWSUtils;
import org.deegree.portal.Constants;
import org.deegree.portal.context.GeneralExtension;
import org.deegree.portal.context.ViewContext;
import org.w3c.dom.Document;

/**
 * Listener class for handling logout from iGeoPortal standard edition
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 */
public class LogoutListener extends AbstractListener {

    private static ILogger LOG = LoggerFactory.getLogger( LogoutListener.class );

    private static final NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    /**
     * performs a login request. the passed event contains a RPC method call containing a sessionID
     * 
     * @param event
     */
    @Override
    public void actionPerformed( FormEvent event ) {
        RPCWebEvent re = (RPCWebEvent) event;

        if ( !validateRequest( re ) ) {
            String s = Messages.getMessage( "IGEO_STD_SEC_INVALID_LOGOUT" );
            LOG.logDebug( s );
            return;
        }

        String user = null;
        try {
            user = performLogout( re );
        } catch ( Exception e ) {
            gotoErrorPage( e.toString() );
            LOG.logDebug( e.getMessage(), e );
            return;
        }

        // write request parameter into session to reconstruct the search form
        HttpSession session = ( (HttpServletRequest) this.getRequest() ).getSession( true );
        session.removeAttribute( "SESSIONID" );
        getRequest().setAttribute( "USER", user );

    }

    /**
     * validates the passed event to be valid agaist the requirements of the listener (contains user
     * name and password)
     * 
     * @param event
     * @return boolean
     */
    private boolean validateRequest( RPCWebEvent event ) {
        RPCMethodCall mc = event.getRPCMethodCall();
        if ( mc.getParameters().length == 0 ) {
            return false;
        }
        String sessionId = (String) mc.getParameters()[0].getValue();
        if ( sessionId == null ) {
            return false;
        }

        return true;
    }

    /**
     * 
     * @return String
     */
    private String getAddress() {
        HttpSession session = ( (HttpServletRequest) getRequest() ).getSession( true );
        ViewContext vc = (ViewContext) session.getAttribute( Constants.CURRENTMAPCONTEXT );
        GeneralExtension ge = vc.getGeneral().getExtension();
        BaseURL baseUrl = ge.getAuthentificationSettings().getAuthentificationURL();
        return NetWorker.url2String( baseUrl.getOnlineResource() );
    }

    /**
     * peforms a logout by sending the sessionID contained in the event to the WAAS like service.
     * The service answers with the id of the session that has been closed and the name of the user
     * who is assigned to the session.<BR>
     * A logout may fails if the passed sessionID is unkown or the session assigned to the ID has
     * already been closed or is expired
     * 
     * @param event
     * @return name of the user assigned to the passed sessionId
     * @throws Exception
     */
    private String performLogout( RPCWebEvent event )
                            throws Exception {
        RPCMethodCall mc = event.getRPCMethodCall();
        String sessionId = (String) mc.getParameters()[0].getValue();
        StringBuffer sb = new StringBuffer( OWSUtils.validateHTTPGetBaseURL( getAddress() ) );
        sb.append( "service=WAS&request=DescribeUser&SESSIONID=" ).append( sessionId );
        URL url = new URL( sb.toString() );
        NetWorker nw = new NetWorker( CharsetUtils.getSystemCharset(), url );
        Reader reader = new InputStreamReader( nw.getInputStream() );
        Document doc = XMLTools.parse( reader );
        String user = XMLTools.getNodeAsString( doc, "/User/UserName", nsContext, null );
        if ( user == null ) {
            throw new Exception( Messages.getMessage( "IGEO_STD_SEC_ERROR_GET_USERNAME", sessionId ) );
        }

        sb = new StringBuffer( OWSUtils.validateHTTPGetBaseURL( getAddress() ) );
        sb.append( "service=WAS&request=CloseSession&SESSIONID=" ).append( sessionId );
        url = new URL( sb.toString() );

        nw = new NetWorker( CharsetUtils.getSystemCharset(), url );
        byte[] b = nw.getDataAsByteArr( 100 );
        if ( b != null ) {
            String tmp = new String( b );
            if ( tmp.trim().length() > 0 ) {
                throw new Exception( Messages.getMessage( "IGEO_STD_SEC_FAIL_LOGOUT", sessionId ) );
            }
        }

        return user;
    }

}
