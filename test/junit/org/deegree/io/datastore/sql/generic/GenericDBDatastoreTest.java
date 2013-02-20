//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/io/datastore/sql/generic/GenericDBDatastoreTest.java $
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
package org.deegree.io.datastore.sql.generic;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.io.datastore.Datastore;
import org.deegree.io.datastore.DatastoreException;
import org.deegree.io.datastore.schema.MappedGMLSchema;
import org.deegree.io.datastore.schema.MappedGMLSchemaDocument;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.feature.FeatureException;
import org.deegree.ogcbase.PropertyPath;
import org.deegree.ogcbase.PropertyPathFactory;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.MissingParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wfs.operation.Query;
import org.deegree.ogcwebservices.wfs.operation.GetFeature.RESULT_TYPE;
import org.xml.sax.SAXException;

import alltests.Configuration;

/**
 * Test case for performing requests against the example datastore.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version 2.0, $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 * 
 * @since 2.0
 */
public class GenericDBDatastoreTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( GenericDBDatastoreTest.class );
    private static final String CONFIG_FILE = "example/featuretypes/genericsql/major_urban_places.xsd";

    private Datastore datastore;

    public static Test suite() {
        return new TestSuite( GenericDBDatastoreTest.class );
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp()
                            throws Exception {
        super.setUp();
        fail( "Not setting up GenerciDBDatastore test -- fixme" );
//        MappedGMLSchemaDocument mappedGMLSchemaDocument = new MappedGMLSchemaDocument();
//        mappedGMLSchemaDocument.load( new URL( Configuration.getWFSBaseDir(), CONFIG_FILE ) );
//        MappedGMLSchema mappedGMLSchema = mappedGMLSchemaDocument.parseMappedGMLSchema();
//        datastore = mappedGMLSchema.getDatastore();

    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown()
                            throws Exception {
        super.tearDown();
        if ( datastore != null ) {
            datastore.close();
        }
        datastore = null;
    }

    public void testQuery1()
                            throws IOException, FeatureException, DatastoreException, URISyntaxException,
                            UnknownCRSException {
        fail( "Not testing Query1 -- fixme" );
//        assertNotNull( datastore );
//
//        URI namespace = new URI( "http://www.deegree.org/app" );
//        PropertyPath pp = PropertyPathFactory.createPropertyPath( new QualifiedName( "app", "name", namespace ) );
//        QualifiedName featureType = new QualifiedName( "app", "major_urban_places", namespace );
//        Query query = Query.create( new PropertyPath[] { pp }, null, null, null, null,
//                                    new QualifiedName[] { featureType }, null, "EPSG:4326", null, -1, 0,
//                                    RESULT_TYPE.RESULTS );
//        long time = System.currentTimeMillis();
//        FeatureCollection result = datastore.performQuery( query, null );
//        LOG.logInfo( System.currentTimeMillis() - time );
//        assertNotNull( result );
//        String fileName = new URL( Configuration.getWFSBaseDir(), Configuration.GENERATED_DIR
//                                                                  + "/GenericDatastoreQueryResult.xml" ).getFile();
//        OutputStream outputStream = new FileOutputStream( fileName );
//        new GMLFeatureAdapter().export( result, outputStream );
//        AllTests.LOG.logInfo( "Wrote '" + fileName + "'." );
    }

    public void testQuery2()
                            throws IOException, FeatureException, InvalidParameterValueException, DatastoreException,
                            SAXException, MissingParameterValueException, OGCWebServiceException, UnknownCRSException {
        fail( "Not testing Query2 -- fixme" );
//        assertNotNull( datastore );
//
//        URL url = new URL( Configuration.getWFSBaseDir()
//                           + "/example/deegree/requests/GetFeature/GetFeatureExample8.xml" );
//        XMLFragment xml = new XMLFragment( url );
//        Document doc = xml.getRootElement().getOwnerDocument();
//        GetFeature gf = GetFeature.create( "ID", doc.getDocumentElement() );
//
//        long time = System.currentTimeMillis();
//        FeatureCollection result = datastore.performQuery( gf.getQuery()[0], null );

//        LOG.logInfo( System.currentTimeMillis() - time );
//
//        assertNotNull( result );
//
//        String fileName = new URL( Configuration.getWFSBaseDir(), Configuration.GENERATED_DIR
//                                                                  + "/GenericDatastoreQueryResult.xml" ).getFile();
//        OutputStream outputStream = new FileOutputStream( fileName );
//        new GMLFeatureAdapter().export( result, outputStream );
//        AllTests.LOG.logInfo( "Wrote '" + fileName + "'." );
    }

}