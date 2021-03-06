//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/sos/configuration/SOSClientConfiguration.java $
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
package org.deegree.portal.standard.sos.configuration;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

/**
 * enables access to the configuration parameters of the sos client. Mainly these are the names and addresses of the
 * sensor observation services to be called
 * 
 * @author <a href="mailto:che@wupperverband.de.de">Christian Heier</a>
 * @author last edited by: $Author: mays$
 * 
 * @version $Revision: 11976 $, $Date: 28.05.2008 17:06:48$
 */
public class SOSClientConfiguration {
    
    private static SOSClientConfiguration conf = null;

    private HashMap<String, URL> sosResources = null;

    /**
     * Creates a new SOSClientConfiguration object.
     * 
     * @param sosResources
     */
    SOSClientConfiguration( HashMap<String, URL> sosResources ) {
        this.sosResources = sosResources;
    }

    /**
     * 
     * 
     * @param confURL
     * 
     * @return SOSClientConfiguration instance
     * 
     * @throws Exception
     */
    public synchronized static SOSClientConfiguration getInstance( URL confURL )
                            throws Exception {
        InputStreamReader isr = new InputStreamReader( confURL.openStream() );
        conf = ConfigurationFactory.createConfiguration( isr );
        isr.close();

        return conf;
    }

    /**
     * 
     * 
     * @return SOSClientConfiguration instance
     */
    public static SOSClientConfiguration getInstance() {
        if ( conf == null ) {
            conf = new SOSClientConfiguration( new HashMap<String, URL>() );
        }

        return conf;
    }

    /**
     * 
     * @param sos
     * @return returns the address of the submitted sensor observation service
     */
    public URL getSOSAddress( String sos ) {
        return this.sosResources.get( sos );
    }

    /**
     * 
     * @return returns the names of the sensor observation services known by the client
     */
    public String[] getSOSNames() {
        return sosResources.keySet().toArray( new String[sosResources.size()] );
    }
}