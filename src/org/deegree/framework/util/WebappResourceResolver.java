//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/WebappResourceResolver.java $
// $Id: WebappResourceResolver.java 10660 2008-03-24 21:39:54Z apoth $
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.servlet.ServletContext;

import org.deegree.framework.log.ILogger;

/**
 * Utility class for resolving of references in webapp config files to {@link URL}s.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 */
public class WebappResourceResolver {

    /**
     * 'Heuristical' method to retrieve the {@link URL} for a file referenced from an init-param of
     * a webapp config file which may be:
     * <ul>
     * <li>a (absolute) <code>URL</code></li>
     * <li>a file location</li>
     * <li>a (relative) URL which in turn is resolved using <code>ServletContext.getRealPath</code>
     * </li>
     * </ul>
     * 
     * @param location
     * @param context
     * @param log
     *            the log where errors are logged
     * @return the full (and whitespace-escaped) URL
     * @throws MalformedURLException
     */
    public static URL resolveFileLocation( String location, ServletContext context, ILogger log )
                            throws MalformedURLException {
        URL serviceConfigurationURL = null;

        log.logDebug( "Resolving configuration file location: '" + location + "'..." );
        try {
            // construction of URI performs whitespace escaping
            serviceConfigurationURL = new URI( location ).toURL();
        } catch ( Exception e ) {
            log.logDebug( "No valid (absolute) URL. Trying context.getRealPath() now." );
            String realPath = context.getRealPath( location );
            if ( realPath == null ) {
                log.logDebug( "No 'real path' available. Trying to parse as a file location now." );
                serviceConfigurationURL = new File( location ).toURI().toURL();
            } else {
                try {
                    // realPath may either be a URL or a File
                    serviceConfigurationURL = new URI( realPath ).toURL();
                } catch ( Exception e2 ) {
                    log.logDebug( "'Real path' cannot be parsed as URL. " + "Trying to parse as a file location now." );
                    // construction of URI performs whitespace escaping
                    serviceConfigurationURL = new File( realPath ).toURI().toURL();
                    log.logDebug( "serviceConfigurationURL: " + serviceConfigurationURL );
                }
            }
        }
        return serviceConfigurationURL;
    }
}
