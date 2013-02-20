//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/SOServiceFactory.java $
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
 Aennchenstraße 19  
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 lat/lon GmbH
 Aennchenstraße 19
 53177 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.ogcwebservices.sos;

import java.io.IOException;
import java.net.URL;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.InvalidConfigurationException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.sos.configuration.SOSConfiguration;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * 
 * @version 1.0
 */

public final class SOServiceFactory {

    private static SOSConfiguration CONFIG;

    private static final ILogger LOG = LoggerFactory.getLogger( SOServiceFactory.class );

    // @todo the factory has a pool of service instances

    /**
     * Hidden constructor
     * 
     * 
     */
    private SOServiceFactory() {
        // nothing to do
    }

    /**
     * must be synchronized
     * 
     * @param scsConfiguration
     * 
     */
    public static void setConfiguration( SOSConfiguration scsConfiguration ) {
        CONFIG = scsConfiguration;

    }

    /**
     * 
     * @param serviceConfigurationUrl
     * @throws InvalidConfigurationException
     */
    public static void setConfiguration( URL serviceConfigurationUrl )
                            throws InvalidConfigurationException {
        try {
            SOServiceFactory.setConfiguration( SOSConfiguration.create( serviceConfigurationUrl ) );
        } catch ( IOException e ) {
            e.printStackTrace();
            throw new InvalidConfigurationException( "SOSFactory", e.getMessage() );
        } catch ( SAXException e ) {
            LOG.logError( e.getMessage(), e );
            throw new InvalidConfigurationException( "SOSFactory", e.getMessage() );
        }

    }

    /**
     * 
     * @return
     * @throws OGCWebServiceException
     */
    public static SOService getService()
                            throws OGCWebServiceException {
        if ( CONFIG == null )
            throw new OGCWebServiceException( SOServiceFactory.class.getName(), "SOS has no configuration" );
        // get an instance of the service from the pool
        return SOService.create( CONFIG ); // @TODO get instance from pool
    }

    /**
     * 
     * @param config
     * @return
     */
    public static SOService getUncachedService( SOSConfiguration config ) {
        return SOService.create( config );
    }

}