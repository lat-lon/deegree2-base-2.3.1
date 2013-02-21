//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/model/feature/GMLFeatureCollectionDocumentTest.java $
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

import static java.io.File.createTempFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.xml.sax.SAXException;

import alltests.Configuration;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version. $Revision: 11917 $, $Date: 2008-05-27 12:07:06 +0200 (Di, 27. Mai 2008) $
 */
public class GMLFeatureCollectionDocumentTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( GMLFeatureCollectionDocumentTest.class );

    private static String FILE_OUT = "wfs/output/GMLFeatureCollectionTest_Output.xml";

    /**
     * @throws IOException
     * @throws SAXException
     * @throws XMLParsingException
     * @throws FeatureException
     */
    public void testParsing()
                            throws IOException, SAXException, XMLParsingException, FeatureException {

        InputStream in = new URL(
                                  "http://www.iai.fzk.de/www-extern/index.php?id=1151&file=QlAyMDcwL0JQMjA3MF8yXzAuemlw" ).openStream();
        File tmp = createTempFile( "gmlziptest", null );
        tmp.deleteOnExit();
        OutputStream out = new FileOutputStream( tmp );

        int read;
        byte[] buf = new byte[16384];
        while ( ( read = in.read( buf ) ) != -1 ) {
            out.write( buf, 0, read );
        }
        in.close();
        out.close();

        ZipFile zip = new ZipFile( tmp );

        ZipEntry entry = zip.entries().nextElement();
        assertNotNull( entry );

        in = zip.getInputStream( entry );

        GMLFeatureCollectionDocument doc = new GMLFeatureCollectionDocument( false );
        doc.load( in, "http://www.systemid.org" );
        FeatureCollection fc = doc.parse();
        URL outputURL = new URL( Configuration.getResourceDir(), FILE_OUT );
        new GMLFeatureAdapter().export( fc, new FileOutputStream( outputURL.getFile() ) );

        LOG.logInfo( "Wrote '" + outputURL.getFile() + "'." );
    }

}
