//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/security/control/GetSessionIDListener.java $
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.deegree.enterprise.control.AbstractListener;
import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;

/**
 * Listener to retrieve the (deegree managed) sessionID of a user.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public class GetSessionIDListener extends AbstractListener {

    private static final ILogger LOG = LoggerFactory.getLogger( GetSessionIDListener.class );

    private static String userDir = "WEB-INF/conf/igeoportal/users/";

    /**
     * performs a login request. the passed event contains a RPC method call containing user name
     * and password.
     * 
     * @param event
     */
    @Override
    public void actionPerformed( FormEvent event ) {

        RPCWebEvent re = (RPCWebEvent) event;

        if ( !validateRequest( re ) ) {
            gotoErrorPage( Messages.getMessage( "IGEO_STD_SEC_MISSING_USER" ) );
            return;
        }

        try {
            // write request parameter into session to reconstruct the search form
            HttpSession session = ( (HttpServletRequest) this.getRequest() ).getSession( true );
            getRequest().setAttribute( "SESSIONID", session.getAttribute( "SESSIONID" ) );
            getRequest().setAttribute( "STARTCONTEXT", getUsersStartContext( re ) );
        } catch ( IOException e ) {
            gotoErrorPage( Messages.getMessage( "IGEO_STD_SEC_ERROR_STARTCONTEXT" ) );
            LOG.logError( e.getMessage(), e );
        }
    }

    /**
     * validates the passed event to be valid agaist the requirements of the listener (contains user
     * name )
     * 
     * @param event
     * @return boolean
     */
    private boolean validateRequest( RPCWebEvent event ) {
        RPCMethodCall mc = event.getRPCMethodCall();
        if ( mc.getParameters().length == 0 ) {
            return false;
        }
        RPCStruct struct = (RPCStruct) mc.getParameters()[0].getValue();
        if ( struct.getMember( "user" ) == null ) {
            return false;
        }

        return true;
    }

    /**
     * returns the name of the users start context. If the user does not own an individual start
     * context the name of the default start context for all users will be returned.
     * 
     * @param event
     * @return String
     * @throws IOException
     */
    private String getUsersStartContext( RPCWebEvent event )
                            throws IOException {
        RPCMethodCall mc = event.getRPCMethodCall();
        RPCStruct struct = (RPCStruct) mc.getParameters()[0].getValue();
        String userName = (String) struct.getMember( "user" ).getValue();
        
        StringBuffer dir = new StringBuffer( "users/" );
        
        StringBuffer sb = new StringBuffer( 300 );
        sb.append( getHomePath() ).append( userDir ).append( userName );
        sb.append( "/context.properties" );
        File file = new File( sb.toString() );
        
        if ( !file.exists() ) {
            sb.delete( 0, sb.length() );
            sb.append( getHomePath() ).append( userDir ).append( "context.properties" );
            file = new File( sb.toString() );
        } else {
            dir.append( userName ).append( '/' );
        }
        
        Properties prop = new Properties();
        InputStream is = file.toURL().openStream();
        prop.load( is );
        is.close();
        
        return dir.append( prop.getProperty( "STARTCONTEXT" ) ).toString();
    }

}
