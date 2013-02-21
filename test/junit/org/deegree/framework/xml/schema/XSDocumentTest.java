//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/framework/xml/schema/XSDocumentTest.java $
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

package org.deegree.framework.xml.schema;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;

import alltests.Configuration;

public class XSDocumentTest extends TestCase {
    
    private static ILogger LOG = LoggerFactory.getLogger( XSDocumentTest.class );
    

    private static final String XSD_FILE = "xml/schema/Philosopher.xsd";

    private static URI NAMESPACE;
    static {
        try {
            NAMESPACE = new URI( "http://www.deegree.org/app" );
        } catch ( URISyntaxException e ) {
            LOG.logError( "Invalid namespace URI: " + e.getMessage(), e );
        }
    }

    private static final QualifiedName FT_PHILOSOPHER_NAME = new QualifiedName( "Philosopher", NAMESPACE );

    private static final QualifiedName FT_PHILOSOPHER_TYPE = new QualifiedName( "PhilosopherType", NAMESPACE );

    private static final QualifiedName FT_PLACE_TYPE = new QualifiedName( "PlaceType", NAMESPACE );

    private XSDocument doc;

    protected void setUp()
                            throws Exception {
        super.setUp();
        URL inputURL = new URL( Configuration.getResourceDir(), XSD_FILE );
        doc = new XSDocument();
        doc.load( inputURL );
    }

    public void testParseSchema()
                            throws XMLParsingException, XMLSchemaException {
        XMLSchema schema = doc.parseXMLSchema();

        ElementDeclaration[] elementDeclaration = schema.getElementDeclarations();
        assertNotNull( elementDeclaration );
        assertEquals( 4, elementDeclaration.length );

        ElementDeclaration singleElement = schema.getElementDeclaration( FT_PHILOSOPHER_NAME );
        assertNotNull( singleElement );
        assertEquals( "Philosopher", singleElement.getName().getPrefixedName() );
        assertEquals( "app:PhilosopherType", singleElement.getType().getName().getPrefixedName() );

        ComplexTypeDeclaration[] complexTypeDeclaration = schema.getComplexTypeDeclarations();
        assertNotNull( complexTypeDeclaration );
        assertEquals( 3, complexTypeDeclaration.length );

        ComplexTypeDeclaration singleComplexType = schema.getComplexTypeDeclaration( FT_PHILOSOPHER_TYPE );
        assertEquals( FT_PHILOSOPHER_TYPE, singleComplexType.getName() );

        singleComplexType = schema.getComplexTypeDeclaration( FT_PLACE_TYPE );
        assertNotNull( singleComplexType );
        assertEquals( FT_PLACE_TYPE, singleComplexType.getName() );

        ElementDeclaration[] subElements = singleComplexType.getExplicitElements();
        assertNotNull( subElements );
        assertEquals( 3, subElements.length );
        assertEquals( "id", subElements[0].getName().getPrefixedName() );
        assertEquals( "xsd:string", subElements[0].getType().getName().getPrefixedName() );

        SimpleTypeDeclaration[] simpleTypeDeclaration = schema.getSimpleTypeDeclarations();
        assertNotNull( simpleTypeDeclaration );
        assertEquals( 0, simpleTypeDeclaration.length );
    }
}
