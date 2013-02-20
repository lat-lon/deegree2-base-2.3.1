//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/context/control/ContextLoadListener.java $
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

package org.deegree.portal.standard.context.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMember;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCParameter;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCUtils;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.portal.PortalException;

/**
 * This listener generates a list of availavle ViewContexts.
 * The only parameter passed is the user name.Currently only 
 * .xml files are being accepted as contexts and it's also
 * also assumed that those are available under 
 * WEB-INF/xml/users/some_user user directories
 *  
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 *
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public class ContextLoadListener extends AbstractContextListener {

    private static String userDir = "WEB-INF/conf/igeoportal/users/";
    private static final ILogger LOG = LoggerFactory.getLogger( ContextLoadListener.class );
    
    /* (non-Javadoc)
     * @see org.deegree.enterprise.control.WebListener#actionPerformed(org.deegree.enterprise.control.FormEvent)
     */
    @Override
    public void actionPerformed( FormEvent event ) {

        RPCWebEvent rpc = (RPCWebEvent) event;
        try {
            validate( rpc );
        } catch ( PortalException e ) {
            LOG.logError( e.getMessage(), e );
            gotoErrorPage( Messages.getMessage( "IGEO_STD_CNTXT_INVALID_RPC", "ContextLoad", e.getMessage() ) );
            return;
        }

        String userName = extractUserName( rpc );
        List contextList =  getContextList( userName );
        
        getRequest().setAttribute( "CONTEXT_LIST", contextList ); 
        getRequest().setAttribute( "USER", userName );

    }

    /**
     * reads the session ID from the passed RPC and gets the assigned
     * user name from a authentification service
     * @param event
     * @return the user name
     */
    private String extractUserName( RPCWebEvent event ) {
        RPCMethodCall mc = event.getRPCMethodCall();
        RPCParameter[] pars = mc.getParameters();
        RPCStruct struct = (RPCStruct) pars[0].getValue();

        String name = "default";
        try {
            name = getUserName( RPCUtils.getRpcPropertyAsString( struct, "sessionID" ) );
            if ( name == null ) {
                name = "default";
            }
        } catch ( Exception e ) {
        }
        // get map context value
        return name;
    }

    /**
     * returns a list of all available context documents assigned to the
     * passed user
     * 
     * @param userName
     * @return the list of available context documents  
     */
    private List<String> getContextList( String userName ) {

        String path2Dir = getHomePath() + userDir + userName;

        File dir = new File( path2Dir );
        File[] files = dir.listFiles();
        List<String> contextList = new ArrayList<String>();
        for ( int i = 0; i < files.length; i++ ) {
            String s = files[i].getName();
            if ( files[i].isFile() && s.endsWith( ".xml" ) ) {
                contextList.add( files[i].getName() );
            }
        }
        String[] list = contextList.toArray(new String[contextList.size()] );
        Arrays.sort( list );
        return new ArrayList<String>( Arrays.asList( list ) );
    }

    /**
     * validates if the passed RPC call containes the required variables
     *  
     * @param rpc
     * @throws PortalException
     */
    private void validate( RPCWebEvent rpc )
                            throws PortalException {
        RPCMethodCall mc = rpc.getRPCMethodCall();
        RPCParameter param = mc.getParameters()[0];
        RPCStruct struct = (RPCStruct) param.getValue();
        RPCMember sessionID = struct.getMember( "sessionID" ); 
        if ( sessionID == null ) {
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_MISSING_PARAM", "sessionID", "ContextLoad") );
        }
    }

}
