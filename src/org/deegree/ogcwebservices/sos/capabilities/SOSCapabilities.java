//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/capabilities/SOSCapabilities.java $
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
package org.deegree.ogcwebservices.sos.capabilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.deegree.ogcwebservices.getcapabilities.Contents;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;
import org.deegree.ogcwebservices.getcapabilities.ServiceIdentification;
import org.deegree.ogcwebservices.getcapabilities.ServiceProvider;
import org.deegree.owscommon.OWSCommonCapabilities;
import org.xml.sax.SAXException;

/**
 * Represents the SOS Capabilities
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * 
 * @version 1.0
 */

public class SOSCapabilities extends OWSCommonCapabilities {

    /**
     * 
     */
    private static final long serialVersionUID = -6878980546920553064L;

    private ArrayList<Sensor> sensorList = new ArrayList<Sensor>();

    private ArrayList<Platform> platformList = new ArrayList<Platform>();

    /**
     * createCapabilities
     * 
     * @param url
     * @return the new instance
     * @throws IOException
     * @throws SAXException
     * @throws InvalidCapabilitiesException
     * 
     */
    public static OGCCapabilities createCapabilities( URL url )
                            throws IOException, SAXException, InvalidCapabilitiesException {
        OGCCapabilities capabilities = null;
        CapabilitiesDocument capabilitiesDoc = new CapabilitiesDocument();
        capabilitiesDoc.load( url );
        capabilities = capabilitiesDoc.parseCapabilities();
        return capabilities;
    }

    /**
     * 
     * @param version
     * @param updateSequence
     * @param serviceIdentification
     * @param serviceProvider
     * @param operationsMetadata
     * @param contents
     * @param platformList
     * @param sensorList
     */
    public SOSCapabilities( String version, String updateSequence, ServiceIdentification serviceIdentification,
                            ServiceProvider serviceProvider, OperationsMetadata operationsMetadata, Contents contents,
                            ArrayList<Platform> platformList, ArrayList<Sensor> sensorList ) {

        super( version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata, contents );

        this.platformList = platformList;

        this.sensorList = sensorList;
    }

    /**
     * 
     * @return sensorList
     */
    public ArrayList<Sensor> getSensorList() {
        return this.sensorList;
    }

    /**
     * 
     * @return platformList
     */
    public ArrayList<Platform> getPlatformList() {
        return this.platformList;
    }

}
