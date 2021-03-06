//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/CSWHarvestingContextListener.java $
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
package org.deegree.enterprise.servlet;

import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.WebappResourceResolver;
import org.deegree.ogcwebservices.csw.CSWFactory;
import org.deegree.ogcwebservices.csw.manager.Manager_2_0_0;

/**
 * This class can be used to start up CSW harvesting thread when a servlet context will be
 * initialized and to free all assigned resources if it will be destroyed.<br>
 * For this it has to be registered to a servelt context making a &lt;listener&gt; entry to web.xml
 * 
 * <pre>
 *  &lt;web-app&gt;
 *   &lt;listener&gt;
 *       &lt;listener-class&gt;com.listeners.MyContextListener&lt;/listener-class&gt;
 *   &lt;/listener&gt;
 *   &lt;servlet&gt;
 *   ...
 *   &lt;/servlet&gt;
 *   &lt;servlet-mapping&gt;
 *    ...
 *   &lt;/servlet-mapping&gt;
 *  &lt;/web-app&gt;
 * &lt;pre&gt;
 * 
 * 
 * @version $Revision: 10660 $
 * @author &lt;a href=&quot;mailto:poth@lat-lon.de&quot;&gt;Andreas Poth&lt;/a&gt;
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 2.0
 * 
 */
public class CSWHarvestingContextListener implements ServletContextListener {

    private static final ILogger LOG = LoggerFactory.getLogger( CSWHarvestingContextListener.class );

    /**
     * @param event
     */
    public void contextDestroyed( ServletContextEvent event ) {
        // Manager.stopAllHarvester();
    }

    /**
     * @param event
     */
    public void contextInitialized( ServletContextEvent event ) {
        ServletContext sc = event.getServletContext();
        String s = sc.getInitParameter( "CSW.config" );
        if ( s == null ) {
            s = sc.getInitParameter( "csw.config" );
        }

        try {
            URL url = WebappResourceResolver.resolveFileLocation( s, sc, LOG );
            CSWFactory.setConfiguration( url );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new RuntimeException( Messages.getString( "CSWHarvestingContextListener.ONINIT" ) );
        }
        Manager_2_0_0.startAllHarvester();
    }

}
