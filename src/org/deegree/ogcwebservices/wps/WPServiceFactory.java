//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/WPServiceFactory.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
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

package org.deegree.ogcwebservices.wps;

import java.io.IOException;
import java.net.URL;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.InvalidConfigurationException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wps.configuration.WPSConfiguration;
import org.deegree.ogcwebservices.wps.configuration.WPSConfigurationDocument;
import org.xml.sax.SAXException;

/**
 * WPServiceFactory.java
 * 
 * Created on 08.03.2006. 17:47:52h
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11310 $, $Date: 2008-04-21 09:55:24 +0200 (Mon, 21 Apr 2008) $
 */
public final class WPServiceFactory {

    private static WPSConfiguration CONFIG = null;

    private static final ILogger LOG = LoggerFactory.getLogger( WPServiceFactory.class );

    private WPServiceFactory() {
        //prevent instantiation
    }

    /**
     * 
     * @param config
     * @return WPService
     */
    public static WPService getInstance( WPSConfiguration config ) {
        return new WPService( config );
    }

    /**
     * 
     * @return WPService
     * @throws OGCWebServiceException
     */
    public static WPService getInstance()
                            throws OGCWebServiceException {
        if ( null == CONFIG ) {
            throw new OGCWebServiceException( WPServiceFactory.class.getName(),
                                              "Configuration has not been initialized" );
        }
        return new WPService( CONFIG );
    }

    /**
     * 
     * @param wpsConfiguration
     */
    public static void setConfiguration( WPSConfiguration wpsConfiguration ) {
        CONFIG = wpsConfiguration;
    }

    /**
     * 
     * @param serviceConfigurationUrl
     * @throws InvalidConfigurationException
     * @throws IOException
     */
    public static void setConfiguration( URL serviceConfigurationUrl )
                            throws InvalidConfigurationException, IOException {

        WPSConfigurationDocument wpsConfDoc = new WPSConfigurationDocument();
        try {
            wpsConfDoc.load( serviceConfigurationUrl );
        } catch ( SAXException e ) {
            LOG.logError( "SAXException: " + e.getMessage() );
            throw new InvalidConfigurationException( "WPServiceFactory", e.getMessage() );
        }
        WPServiceFactory.setConfiguration( wpsConfDoc.getConfiguration() );

    }

    /**
     * 
     * @return WPService
     */
    public static WPService getService() {
        return WPServiceFactory.getInstance( CONFIG );
    }

}
