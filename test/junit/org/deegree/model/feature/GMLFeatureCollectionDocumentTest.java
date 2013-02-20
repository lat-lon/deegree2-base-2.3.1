//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/model/feature/GMLFeatureCollectionDocumentTest.java $
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
package org.deegree.model.feature;

import java.io.IOException;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version. $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 */
public class GMLFeatureCollectionDocumentTest extends TestCase {
    
    private static ILogger LOG = LoggerFactory.getLogger( GMLFeatureCollectionDocumentTest.class );

    private static final String XML_FILE = "/home/markus/workspace/bplan/resources/bp2070_v2.1/BP2070_2.gml";

    private static String FILE_OUT = "wfs/output/BPlan2070_Output.xml";

    protected void setUp()
                            throws Exception {
        super.setUp();

        // XMLFragment xml = new XMLFragment();
        // URL inputURL = new URL( Configuration.getResourceDir(), XML_FILE );
        // xml.load( inputURL );
        // Map xsd = xml.getAttachedSchemas();
        // assertNotNull( xsd );
        // GMLSchemaDocument[] schemas = new GMLSchemaDocument[xsd.size()];
        // doc = new GMLSchema[xsd.size()];
        // Set entries = xsd.entrySet();
        // assertNotNull( entries );
        // Iterator it = entries.iterator();
        // int i = 0;
        // while (it.hasNext()) {
        // Map.Entry entry = (Map.Entry) it.next();
        // URL schemaURL = (URL) entry.getValue();
        // schemas[i] = new GMLSchemaDocument();
        // schemas[i].load( schemaURL );
        // assertNotNull( schemas[i] );
        // doc[i] = schemas[i].parseGMLSchema();
        // i++;
        // }
    }

    public void testParsing()
                            throws IOException, SAXException, XMLParsingException,
                            FeatureException {
        fail( "Not testing parsing -- fixme" );
//        URL inputURL = new URL( Configuration.getResourceDir(), XML_FILE );
//
//        GMLFeatureCollectionDocument doc = new GMLFeatureCollectionDocument( false );
//        doc.load( inputURL );
//        FeatureCollection fc = doc.parse();
//        URL outputURL = new URL( Configuration.getResourceDir(), FILE_OUT );
//        new GMLFeatureAdapter().export( fc, new FileOutputStream( outputURL.getFile() ) );
//
//        AllTests.LOG.logInfo( "Wrote '" + outputURL.getFile() + "'." );
    }

    protected void tearDown()
                            throws Exception {
        super.tearDown();
    }

}
