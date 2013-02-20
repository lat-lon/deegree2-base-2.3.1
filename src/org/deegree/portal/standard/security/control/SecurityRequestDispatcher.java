//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/security/control/SecurityRequestDispatcher.java $
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
package org.deegree.portal.standard.security.control;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.deegree.enterprise.control.RequestDispatcher;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.io.IODocument;
import org.deegree.io.JDBCConnection;
import org.deegree.security.drm.SecurityAccessManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public class SecurityRequestDispatcher extends RequestDispatcher {

    private static String SECURITY_CONFIG = "Security.configFile";

    /**
     * This method initializes the servlet.
     * 
     * @param cfg
     *            the servlet configuration
     * @throws ServletException
     *             an exception
     */
    @Override
    public void init( ServletConfig cfg )
                            throws ServletException {
        super.init( cfg );

        try {
            // config file -> DOM
            String s = getInitParameter( SECURITY_CONFIG );
            File file = new File( s );
            if ( !file.isAbsolute() ) {
                file = new File( getServletContext().getRealPath( s ) );
            }
            URL url = file.toURL();
            Reader reader = new InputStreamReader( url.openStream() );
            Document doc = XMLTools.parse( reader );
            Element element = doc.getDocumentElement();
            reader.close();

            // extract configuration information from DOM
            String readWriteTimeoutString = XMLTools.getStringValue( "readWriteTimeout", null,
                                                                     element, "600" );
            int readWriteTimeout = Integer.parseInt( readWriteTimeoutString );

            String registryClass = XMLTools.getRequiredStringValue( "registryClass", null, element );
            Element registryConfig = (Element) XMLTools.getRequiredNode( element, "registryConfig",
                                                                         null );

            // required: <connection>
            NamespaceContext nsc = new NamespaceContext();
            nsc.addNamespace( "jdbc", new URI( "http://www.deegree.org/jdbc" ) );
            element = (Element) XMLTools.getRequiredNode( registryConfig, "jdbc:JDBCConnection",
                                                          nsc );
            IODocument xml = new IODocument( element );
            JDBCConnection jdbc = xml.parseJDBCConnection();

            Properties properties = new Properties();

            // required: <driver>
            properties.put( "driver", jdbc.getDriver() );
            // required: <logon>
            properties.put( "url", jdbc.getURL() );
            // required: <user>
            properties.put( "user", jdbc.getUser() );
            // required: <password>
            properties.put( "password", jdbc.getPassword() );

            if ( !SecurityAccessManager.isInitialized() ) {
                SecurityAccessManager.initialize( registryClass, properties,
                                                  readWriteTimeout * 1000 );
            }

        } catch ( Exception e ) {
            throw new ServletException( Messages.getMessage( "IGEO_STD_SEC_FAIL_INIT_SEC_DISPATCHER",
                                                             e.getMessage() ) );
        }

    }
}