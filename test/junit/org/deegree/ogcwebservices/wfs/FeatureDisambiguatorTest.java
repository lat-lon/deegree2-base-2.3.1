//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wfs/FeatureDisambiguatorTest.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
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
package org.deegree.ogcwebservices.wfs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.schema.XMLSchemaException;
import org.deegree.io.datastore.DatastoreException;
import org.deegree.io.datastore.schema.MappedGMLSchema;
import org.deegree.io.datastore.schema.MappedGMLSchemaDocument;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.GMLFeatureAdapter;
import org.deegree.model.feature.GMLFeatureCollectionDocument;
import org.deegree.model.feature.Validator;
import org.deegree.model.feature.schema.FeatureType;
import org.xml.sax.SAXException;

import alltests.AllTests;
import alltests.Configuration;

/**
 * Test case that tries to disambiguate some example feature collections with anonymous features
 * (that are equal to other features in the collection and have to be merged).
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Di, 01. Apr 2008) $
 */
public class FeatureDisambiguatorTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( FeatureDisambiguatorTest.class );
    private MappedGMLSchema schema;

    private Validator validator;

    private URL schemaURL = new URL( Configuration.getWFSBaseDir(),
                                     "example/philosopher/featuretypes/Philosopher.xsd" );

    private URL outputDir = new URL( Configuration.getWFSBaseDir(), Configuration.GENERATED_DIR
                                                                    + "/" );

    private static final String TEST_FILE1 = "featureCollection.xml";

    private static final String TEST_FILE2 = "cyclicFeatureCollection.xml";

    private static final String TEST_FILE3 = "erroneousFeatureCollection.xml";

    public FeatureDisambiguatorTest() throws IOException, SAXException, XMLSchemaException,
                            XMLParsingException, UnknownCRSException {

        MappedGMLSchemaDocument schemaDoc = new MappedGMLSchemaDocument();
        schemaDoc.load( schemaURL );
        schema = schemaDoc.parseMappedGMLSchema();

        // build feature type map
        Map<QualifiedName, FeatureType> ftMap = new HashMap<QualifiedName, FeatureType>();
        FeatureType[] featureTypes = schema.getFeatureTypes();
        for ( FeatureType type : featureTypes ) {
            ftMap.put( type.getName(), type );
        }

        validator = new Validator( ftMap );
    }

    public void testRootFeatureDisambiguation()
                            throws Exception {
        GMLFeatureCollectionDocument fcDoc = new GMLFeatureCollectionDocument( false );
        fcDoc.load( FeatureDisambiguatorTest.class.getResource( TEST_FILE1 ) );
        FeatureCollection fc = fcDoc.parse();
        for ( int i = 0; i < fc.size(); i++ ) {
            Feature feature = fc.getFeature( i );
            LOG.logInfo( "Validating feature '" + feature.getId() );
            validator.validate( feature );
        }

        fc = new FeatureDisambiguator( fc ).mergeFeatures();

        String outputFile = new URL( outputDir, "disambiguated_fc1.xml" ).getFile();
        OutputStream os = new FileOutputStream( outputFile );
        GMLFeatureAdapter adapter = new GMLFeatureAdapter();
        adapter.export( fc, os );
        os.close();
        LOG.logInfo( "Wrote '" + outputFile + "'." );
    }

    public void testCyclicDisambiguation()
                            throws Exception {
        GMLFeatureCollectionDocument fcDoc = new GMLFeatureCollectionDocument( false );
        fcDoc.load( FeatureDisambiguatorTest.class.getResource( TEST_FILE2 ) );
        FeatureCollection fc = fcDoc.parse();
        for ( int i = 0; i < fc.size(); i++ ) {
            Feature feature = fc.getFeature( i );
            LOG.logInfo( "Validating feature '" + feature.getId() );
            validator.validate( feature );
        }

        fc = new FeatureDisambiguator( fc ).mergeFeatures();

        String outputFile = new URL( outputDir, "disambiguated_fc2.xml" ).getFile();
        OutputStream os = new FileOutputStream( outputFile );
        GMLFeatureAdapter adapter = new GMLFeatureAdapter();
        adapter.export( fc, os );
        os.close();
        LOG.logInfo( "Wrote '" + outputFile + "'." );
    }

    public void testUnsuccessfulDisambiguation()
                            throws Exception {
        GMLFeatureCollectionDocument fcDoc = new GMLFeatureCollectionDocument( false );
        fcDoc.load( FeatureDisambiguatorTest.class.getResource( TEST_FILE3 ) );
        FeatureCollection fc = fcDoc.parse();
        for ( int i = 0; i < fc.size(); i++ ) {
            Feature feature = fc.getFeature( i );
            LOG.logInfo( "Validating feature '" + feature.getId() );
            validator.validate( feature );
        }

        try {
            fc = new FeatureDisambiguator( fc ).mergeFeatures();
            fail( "Disambiguation did not fail, but must!" );
        } catch ( DatastoreException e ) {
            LOG.logInfo( "OK - disambiguation failed (and must fail): " + e.getMessage() );
        }
    }
}