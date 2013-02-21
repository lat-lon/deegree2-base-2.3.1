//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/sos/configuration/ConfigurationFactory.java $

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

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import org.deegree.framework.xml.ElementList;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:che@wupperverband.de.de">Christian Heier</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 */
public class ConfigurationFactory {
    private static final URI SOSNS = CommonNamespaces.buildNSURI( "http://www.deegree.org/sosclient" );

    /**
     * 
     * 
     * @param confFile
     * 
     * @return Configuration
     * 
     * @throws SAXException
     * @throws IOException
     * @throws Exception
     */
    public static SOSClientConfiguration createConfiguration( String confFile )
                            throws SAXException, IOException, Exception {

        Reader reader = new FileReader( confFile );
        SOSClientConfiguration conf = createConfiguration( reader );
        reader.close();

        return conf;
    }

    /**
     * 
     * 
     * @param reader
     * 
     * @return SOSClientConfiguration
     * 
     * @throws SAXException
     * @throws IOException
     * @throws Exception
     */
    public static SOSClientConfiguration createConfiguration( Reader reader )
                            throws SAXException, IOException, Exception {

        Document doc = XMLTools.parse( reader );

        // sos descriptions
        Element element = doc.getDocumentElement();
        ElementList el = XMLTools.getChildElements( "sos", SOSNS, element );
        HashMap<String, URL> sosServers = createSOSDesc( el );

        return new SOSClientConfiguration( sosServers );
    }

    /**
     * creates a map of sos names and associated online resources
     */
    private static HashMap<String, URL> createSOSDesc( ElementList nl )
                            throws MalformedURLException {

        HashMap<String, URL> sosServers = new HashMap<String, URL>();

        for ( int i = 0; i < nl.getLength(); i++ ) {
            Element element = nl.item( i );
            Node node = element.getElementsByTagNameNS( SOSNS.toString(), "name" ).item( 0 );
            String name = node.getFirstChild().getNodeValue();
            node = element.getElementsByTagNameNS( SOSNS.toString(), "onlineResource" ).item( 0 );

            String tmp = XMLTools.getStringValue( node );
            sosServers.put( name, new URL( tmp ) );
        }

        return sosServers;
    }

}