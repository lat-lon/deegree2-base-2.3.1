//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/tools/shape/GML2Shape_new.java $
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
package org.deegree.tools.shape;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.io.dbaseapi.DBaseException;
import org.deegree.io.shpapi.shape_new.ShapeFile;
import org.deegree.io.shpapi.shape_new.ShapeFileWriter;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.GMLFeatureCollectionDocument;
import org.deegree.model.spatialschema.GeometryException;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @version $Revision: 12812 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 12812 $, $Date: 2008-07-10 14:07:08 +0200 (Do, 10. Jul 2008) $
 * 
 * @since 1.1
 */
public class GML2Shape_new {

    private static final ILogger LOG = LoggerFactory.getLogger( GML2Shape_new.class );

    private FeatureCollection fc = null;

    private String inName = null;

    private String outName = null;

    /**
     * @param inName
     * @param outName
     */
    public GML2Shape_new( String inName, String outName ) {
        if ( outName.endsWith( ".shp" ) ) {
            outName = outName.substring( 0, outName.lastIndexOf( "." ) );
        }
        this.inName = inName;
        this.outName = outName;
    }

    /**
     * @throws IOException
     * @throws SAXException
     * @throws XMLParsingException
     */
    public void read()
                            throws IOException, SAXException, XMLParsingException {
        LOG.logInfo( "Reading " + inName + " ... " );
        GMLFeatureCollectionDocument doc = new GMLFeatureCollectionDocument();
        URL url;
        try {
            url = new URL( inName );
        } catch ( MalformedURLException e ) {
            url = new File( inName ).toURL();
        }
        doc.load( url.openStream(), url.toString() );
        fc = doc.parse();
    }

    /**
     * @throws GeometryException
     * @throws DBaseException
     * @throws IOException
     * 
     */
    public void write()
                            throws DBaseException, GeometryException, IOException {
        LOG.logInfo( "Writing " + outName + " ... " );
        ShapeFile sf = new ShapeFile( fc, outName );
        ShapeFileWriter writer = new ShapeFileWriter( sf );
        writer.write();
        LOG.logInfo( "Done." );
    }

    /**
     * This is a command line tool.
     * 
     * @param args
     */
    public static void main( String[] args ) {
        try {

            if ( args == null || args.length < 2 ) {
                System.out.println( "Usage: java -cp ... ...GML2Shape_new <inputfile/URL> <outputfile basename>" );
                System.exit( 1 );
            }

            GML2Shape_new rws = new GML2Shape_new( args[0], args[1] );
            rws.read();
            rws.write();

        } catch ( IOException e ) {
            LOG.logError( "IO error occured.", e );
        } catch ( SAXException e ) {
            LOG.logError( "The GML file could not be parsed.", e );
        } catch ( XMLParsingException e ) {
            LOG.logError( "The GML file could not be parsed.", e );
        } catch ( DBaseException e ) {
            LOG.logError( "Could not create .dbf for shapefile.", e );
        } catch ( GeometryException e ) {
            LOG.logError( "A geometry was faulty.", e );
        }
    }
}
