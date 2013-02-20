//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/capabilities/CapabilitiesDocument.java $
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.ElementList;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.getcapabilities.Operation;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;
import org.deegree.owscommon.OWSCommonCapabilitiesDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Read the SOS Capabilities form a XML File
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * 
 * @version 1.0
 */
public class CapabilitiesDocument extends OWSCommonCapabilitiesDocument {

    private static final long serialVersionUID = 1L;

    private static final String XML_TEMPLATE = "SOSCapabilitiesTemplate.xml";

    protected static final URI SCSNS = CommonNamespaces.SOSNS;

    private static final ILogger LOG = LoggerFactory.getLogger( CapabilitiesDocument.class );

    /**
     * creates an empty Document from template file
     * 
     */
    public void createEmptyDocument() throws IOException, SAXException {

        URL url = CapabilitiesDocument.class.getResource( XML_TEMPLATE );
        if ( url == null ) {
            throw new IOException( "The resource '"
                + XML_TEMPLATE + " could not be found." );
        }
        this.load( url );
    }

    public OGCCapabilities parseCapabilities() throws InvalidCapabilitiesException {
        try {
            return new SOSCapabilities( parseVersion(), parseUpdateSequence(),
                getServiceIdentification(), getServiceProvider(), getOperationsMetadata(), null,
                getPlatformList(), getSensorList() );

        } catch (Exception e) {
            throw new InvalidCapabilitiesException( "Class representation of the SOS capabilities "
                + "document could not be generated: " + e.getMessage() );
        }
    }

    /**
     * getOperationsMetadata
     * 
     * @return
     * @throws XMLParsingException
     */
    public OperationsMetadata getOperationsMetadata() throws XMLParsingException {

        Node root = this.getRootElement();
        Element child = XMLTools.getRequiredChildElement( "OperationsMetadata", OWSNS, root );
        ElementList elementList = XMLTools.getChildElements( "Operation", OWSNS, child );

        // build HashMap of 'Operation'-elements for easier access
        HashMap<String,Element> operations = new HashMap<String,Element>();
        for (int i = 0; i < elementList.getLength(); i++) {
            String attrValue = XMLTools.getRequiredAttrValue( "name", null, elementList.item( i ) );
            operations.put( attrValue, elementList.item( i ) );
        }

        // 'GetCapabilities'-operation
        Operation getCapabilites = getOperation( OperationsMetadata.GET_CAPABILITIES_NAME, true,
            operations );
        // 'DescribePlatform'-operation
        Operation describePlatform = getOperation( SOSOperationsMetadata.DESCRIBE_PLATFORM_NAME,
            true, operations );
        // 'DescribeSensor'-operation
        Operation describeSensor = getOperation( SOSOperationsMetadata.DESCRIBE_SENSOR_NAME, false,
            operations );
        // 'GetObservation'-operation
        Operation getObservation = getOperation( SOSOperationsMetadata.GET_OBSERVATION_NAME, true,
            operations );

        return new SOSOperationsMetadata( getCapabilites, describePlatform, describeSensor,
            getObservation );
    }

    /**
     * gets all platforms from the capabilities document
     * 
     * @return
     * @throws XMLParsingException
     */
    protected ArrayList<Platform> getPlatformList() throws XMLParsingException {

        ArrayList<Platform> platformList = new ArrayList<Platform>( 100 );

        Element platformListElement = XMLTools.getRequiredChildElement( "PlatformList", SCSNS, getRootElement() );

        if ( platformListElement != null ) {
            ElementList platformElements = XMLTools.getChildElements( "Platform", SCSNS, platformListElement );

            if ( ( platformElements != null )
                && ( platformElements.getLength() > 0 ) ) {

                for (int i = 0; i < platformElements.getLength(); i++) {
                    String platformId = XMLTools.getRequiredAttrValue( "Id", null, platformElements
                        .item( i ) );
                    String description = XMLTools.getAttrValue( platformElements.item( i ), null, "Description", null );
                    Platform temp = new Platform( platformId, description );
                    platformList.add( temp );
                }

            } else {
                LOG.logWarning( "no Platforms found in the capabilities Document" );
            }
        } else {
            LOG.logWarning( "no Platforms found in the capabilities Document" );
        }

        return platformList;
    }

    /**
     * gets all sensors from the capabilities document
     * 
     * 
     */
    protected ArrayList<Sensor> getSensorList() throws XMLParsingException {
        ArrayList<Sensor> sensorList = new ArrayList<Sensor>();

        Element sensorListElement = XMLTools.getRequiredChildElement( "SensorList", SCSNS, getRootElement() );

        if ( sensorListElement != null ) {

            ElementList sensorElements = XMLTools.getChildElements( "Sensor", SCSNS, sensorListElement );

            if ( ( sensorElements != null )
                && ( sensorElements.getLength() > 0 ) ) {

                for (int i = 0; i < sensorElements.getLength(); i++) {
                    String sensorId = XMLTools.getRequiredAttrValue( "Id", null, sensorElements
                        .item( i ) );
                    String description = XMLTools.getAttrValue( sensorElements.item( i ), null, "Description", null );
                    Sensor temp = new Sensor( sensorId, description );
                    sensorList.add( temp );
                }
            } else {
                LOG.logWarning( "no Sensors found in the capabilities Document" );
            }

        } else {
            LOG.logWarning( "no Sensors found in the capabilities Document" );
        }

        return sensorList;
    }

}