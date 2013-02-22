//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/shpapi/shape_new/ShapeFileReader.java $
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
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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
package org.deegree.io.shpapi.shape_new;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.io.dbaseapi.DBaseException;
import org.deegree.io.dbaseapi.DBaseFile;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.feature.Feature;
import org.deegree.model.spatialschema.ByteUtils;

/**
 * <code>ShapeFileReader</code> is a class to read shapefiles.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: otonnhofer $
 * 
 * @version $Revision: 9734 $, $Date: 2008-01-24 11:43:21 +0100 (Do, 24. Jan 2008) $
 */
public class ShapeFileReader {

    private static final ILogger LOG = LoggerFactory.getLogger( ShapeFileReader.class );

    private String baseName;

    private CoordinateSystem defaultCRS;

    private int shapeType;

    private ShapeEnvelope envelope;

    private int length;

    /**
     * Does not read it yet - just initializes the object.
     * 
     * @param baseName
     */
    public ShapeFileReader( String baseName ) {
        if ( baseName.endsWith( ".shp" ) ) {
            baseName = baseName.substring( 0, baseName.length() - 4 );
        }
        this.baseName = baseName;
    }

    /**
     * Does not read it yet - just initializes the object.
     * 
     * @param baseName
     * @param defaultCRS
     *            CoordinateSystem for the shape file
     */
    public ShapeFileReader( String baseName, CoordinateSystem defaultCRS ) {
        this( baseName );
        this.defaultCRS = defaultCRS;
    }

    private void readHeader( InputStream in )
                            throws IOException {
        byte[] header = new byte[100];

        if ( in.read( header ) != 100 ) {
            LOG.logError( "Header is too small, unexpected things might happen!" );
            return;
        }

        int fileType = ByteUtils.readBEInt( header, 0 );
        if ( fileType != ShapeFile.FILETYPE ) {
            LOG.logWarning( "File type is wrong, unexpected things might happen, continuing anyway..." );
        }

        length = ByteUtils.readBEInt( header, 24 ) * 2; // 16 bit words...

        int version = ByteUtils.readLEInt( header, 28 );
        if ( version != ShapeFile.VERSION ) {
            LOG.logWarning( "File version is wrong, continuing in the hope of compatibility..." );
        }

        shapeType = ByteUtils.readLEInt( header, 32 );

        envelope = new ShapeEnvelope( false, false );
        envelope.read( header, 36 );

        // it shouldn't hurt to write these values as doubles default to 0.0
        // anyway
        double zmin = ByteUtils.readLEDouble( header, 68 );
        double zmax = ByteUtils.readLEDouble( header, 76 );
        double mmin = ByteUtils.readLEDouble( header, 84 );
        double mmax = ByteUtils.readLEDouble( header, 92 );

        switch ( shapeType ) {
        case ShapeFile.NULL:
        case ShapeFile.POINT:
        case ShapeFile.POLYLINE:
        case ShapeFile.POLYGON:
        case ShapeFile.MULTIPOINT:
            break;

        case ShapeFile.POINTM:
        case ShapeFile.POLYLINEM:
        case ShapeFile.POLYGONM:
        case ShapeFile.MULTIPOINTM:
            envelope.extend( mmin, mmax );
            break;

        case ShapeFile.POINTZ:
        case ShapeFile.POLYLINEZ:
        case ShapeFile.POLYGONZ:
        case ShapeFile.MULTIPOINTZ:
        case ShapeFile.MULTIPATCH:
            envelope.extend( zmin, zmax, mmin, mmax );

        }
    }

    private LinkedList<Shape> readShapes( InputStream in, boolean strict )
                            throws IOException {
        LinkedList<Shape> shapes = new LinkedList<Shape>();

        int offset = 0;
        byte[] bytes = new byte[length - 100];
        // read the whole file at once, this makes the "parsing" pretty fast
        in.read( bytes );

        while ( offset < bytes.length ) {
            if ( shapes.size() % 10000 == 0 ) {
                System.out.print( shapes.size() + " shapes read.\r" );
            }

            // ByteUtils.readBEInt( hdr, 0 ); // just ignore the record number
            int len = ByteUtils.readBEInt( bytes, offset + 4 ) * 2;
            offset += 8;

            if ( strict ) {
                Shape s = null;
                switch ( shapeType ) {
                case ShapeFile.NULL:
                    break;
                case ShapeFile.POINT:
                    s = new ShapePoint( false, false, defaultCRS );
                    break;
                case ShapeFile.POINTM:
                    s = new ShapePoint( false, true, defaultCRS );
                    break;
                case ShapeFile.POINTZ:
                    s = new ShapePoint( true, false, defaultCRS );
                    break;
                case ShapeFile.POLYLINE:
                    s = new ShapePolyline( false, false, defaultCRS );
                    break;
                case ShapeFile.POLYLINEM:
                    s = new ShapePolyline( false, true, defaultCRS );
                    break;
                case ShapeFile.POLYLINEZ:
                    s = new ShapePolyline( true, false, defaultCRS );
                    break;
                case ShapeFile.POLYGON:
                    s = new ShapePolygon( false, false, defaultCRS );
                    break;
                case ShapeFile.POLYGONM:
                    s = new ShapePolygon( false, true, defaultCRS );
                    break;
                case ShapeFile.POLYGONZ:
                    s = new ShapePolygon( true, false, defaultCRS );
                    break;
                case ShapeFile.MULTIPOINT:
                    s = new ShapeMultiPoint( false, false, defaultCRS );
                    break;
                case ShapeFile.MULTIPOINTM:
                    s = new ShapeMultiPoint( false, true, defaultCRS );
                    break;
                case ShapeFile.MULTIPOINTZ:
                    s = new ShapeMultiPoint( true, false, defaultCRS );
                    break;
                case ShapeFile.MULTIPATCH:
                    s = new ShapeMultiPatch( len, defaultCRS );
                    break;
                }

                LOG.logDebug( "Reading shape type " + s.getClass().getSimpleName() );

                int alen = s.read( bytes, offset ) - offset;

                if ( len != alen ) {
                    LOG.logWarning( "Length is supposedly " + len + ", actual read length was " + alen );
                    // broken files that omit the M-section and that add the
                    // record length to the
                    // length header:
                    offset += len - 8;
                } else {
                    offset += len;
                }

                shapes.add( s );

            } else {
                // TODO
            }
        }

        LOG.logInfo( "Read " + shapes.size() + " shapes in total." );

        return shapes;
    }

    private Iterator<Shape> iterateShapes( final InputStream in, boolean strict )
                            throws IOException {
        final class Counter {
            int offset = 0;
        }
        final Counter counter = new Counter();

        return new Iterator<Shape>() {
            @Override
            public boolean hasNext() {
                return counter.offset < ( length - 100 );
            }

            @Override
            public Shape next() {
                try {
                    byte[] bytes = new byte[4];

                    in.skip( 4 ); // ignore the record number
                    in.read( bytes, 0, 4 );

                    int len = ByteUtils.readBEInt( bytes, 0 ) * 2;
                    counter.offset += 8;
                    counter.offset += len;
                    bytes = new byte[len];
                    in.read( bytes );

                    Shape s = null;
                    switch ( shapeType ) {
                    case ShapeFile.NULL:
                        break;
                    case ShapeFile.POINT:
                        s = new ShapePoint( false, false, defaultCRS );
                        break;
                    case ShapeFile.POINTM:
                        s = new ShapePoint( false, true, defaultCRS );
                        break;
                    case ShapeFile.POINTZ:
                        s = new ShapePoint( true, false, defaultCRS );
                        break;
                    case ShapeFile.POLYLINE:
                        s = new ShapePolyline( false, false, defaultCRS );
                        break;
                    case ShapeFile.POLYLINEM:
                        s = new ShapePolyline( false, true, defaultCRS );
                        break;
                    case ShapeFile.POLYLINEZ:
                        s = new ShapePolyline( true, false, defaultCRS );
                        break;
                    case ShapeFile.POLYGON:
                        s = new ShapePolygon( false, false, defaultCRS );
                        break;
                    case ShapeFile.POLYGONM:
                        s = new ShapePolygon( false, true, defaultCRS );
                        break;
                    case ShapeFile.POLYGONZ:
                        s = new ShapePolygon( true, false, defaultCRS );
                        break;
                    case ShapeFile.MULTIPOINT:
                        s = new ShapeMultiPoint( false, false, defaultCRS );
                        break;
                    case ShapeFile.MULTIPOINTM:
                        s = new ShapeMultiPoint( false, true, defaultCRS );
                        break;
                    case ShapeFile.MULTIPOINTZ:
                        s = new ShapeMultiPoint( true, false, defaultCRS );
                        break;
                    case ShapeFile.MULTIPATCH:
                        s = new ShapeMultiPatch( len, defaultCRS );
                        break;
                    }

                    s.read( bytes, 0 );

                    return s;
                } catch ( IOException e ) {
                    // ignore
                }
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException( "Removing not supported." );
            }
        };
    }

    /**
     * @return the shape file contents.
     * @throws IOException
     */
    public ShapeFile read()
                            throws IOException {
        File mainFile = new File( baseName + ".shp" );
        BufferedInputStream mainIn = new BufferedInputStream( new FileInputStream( mainFile ) );
        readHeader( mainIn );

        LinkedList<Shape> shapes = readShapes( mainIn, true );

        DBaseFile dbf = new DBaseFile( baseName );

        return new ShapeFile( shapes, envelope, dbf, baseName );
    }

    public Iterator<Feature> iterator()
                            throws IOException, DBaseException {
        File mainFile = new File( baseName + ".shp" );
        BufferedInputStream mainIn = new BufferedInputStream( new FileInputStream( mainFile ) );
        readHeader( mainIn );

        Iterator<Shape> shapes = iterateShapes( mainIn, true );
        DBaseFile dbf = new DBaseFile( baseName );
        ShapeFile shapeFile = new ShapeFile( shapes, envelope, dbf, baseName );

        return shapeFile.iterator();
    }

    /**
     * @return the dbase file
     * @throws IOException
     */
    public DBaseFile getTables()
                            throws IOException {
        return new DBaseFile( baseName );
    }

    /**
     * @return the number of shapes stored in this shape file.
     * @throws IOException
     */
    public int getShapeCount()
                            throws IOException {
        File file = new File( baseName + ".shx" );
        BufferedInputStream in = new BufferedInputStream( new FileInputStream( file ) );
        readHeader( in );
        return ( length - 100 ) / 8;
    }

    /**
     * @return the type of the shape file's contents.
     * @throws IOException
     */
    public int getShapeType()
                            throws IOException {
        File file = new File( baseName + ".shx" );
        BufferedInputStream in = new BufferedInputStream( new FileInputStream( file ) );
        readHeader( in );
        return shapeType;
    }

    public ShapeEnvelope getEnvelope() {
        return envelope;
    }

}
