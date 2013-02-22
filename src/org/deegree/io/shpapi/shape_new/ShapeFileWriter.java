//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/shpapi/shape_new/ShapeFileWriter.java $
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.Pair;
import org.deegree.io.dbaseapi.DBaseException;
import org.deegree.model.spatialschema.ByteUtils;

/**
 * <code>ShapeFileWriter</code> is a class to write shapefiles.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 12812 $, $Date: 2008-07-10 14:07:08 +0200 (Do, 10. Jul 2008) $
 */
public class ShapeFileWriter {

    private ShapeFile shapeFile;

    private static final ILogger LOG = LoggerFactory.getLogger( ShapeFileWriter.class );

    private String name;

    private int offset;

    private int recordNum;

    /**
     * @param shapes
     */
    public ShapeFileWriter( ShapeFile shapes ) {
        shapeFile = shapes;
    }

    public ShapeFileWriter( String name ) {
        this.name = name;
    }

    private void writeHeader( OutputStream out, int length )
                            throws IOException {

        // what's funny about shapefiles:
        // 1) in the headers, they use big endian for some values, little endian for others
        // (the rest of the file is little endian)
        // 2) Only 4 byte ints and 8 byte doubles are used in the file, however,
        // the size is measured in 16 bit words...

        byte[] header = new byte[100];

        ByteUtils.writeBEInt( header, 0, ShapeFile.FILETYPE );
        ByteUtils.writeBEInt( header, 24, length );
        ByteUtils.writeLEInt( header, 28, ShapeFile.VERSION );
        ByteUtils.writeLEInt( header, 32, shapeFile.getShapeType() );

        ShapeEnvelope envelope = shapeFile.getEnvelope();
        envelope.write( header, 36 );

        // it shouldn't hurt to write these values as doubles default to 0.0 anyway
        ByteUtils.writeLEDouble( header, 68, envelope.zmin );
        ByteUtils.writeLEDouble( header, 76, envelope.zmax );
        ByteUtils.writeLEDouble( header, 84, envelope.mmin );
        ByteUtils.writeLEDouble( header, 92, envelope.mmax );

        out.write( header, 0, 100 );
    }

    private static void writeHeader( OutputStream out, int length, int shapeType, ShapeEnvelope envelope )
                            throws IOException {

        // what's funny about shapefiles:
        // 1) in the headers, they use big endian for some values, little endian for others
        // (the rest of the file is little endian)
        // 2) Only 4 byte ints and 8 byte doubles are used in the file, however,
        // the size is measured in 16 bit words...

        byte[] header = new byte[100];

        ByteUtils.writeBEInt( header, 0, ShapeFile.FILETYPE );
        ByteUtils.writeBEInt( header, 24, length );
        ByteUtils.writeLEInt( header, 28, ShapeFile.VERSION );
        ByteUtils.writeLEInt( header, 32, shapeType );

        envelope.write( header, 36 );

        // it shouldn't hurt to write these values as doubles default to 0.0 anyway
        ByteUtils.writeLEDouble( header, 68, envelope.zmin );
        ByteUtils.writeLEDouble( header, 76, envelope.zmax );
        ByteUtils.writeLEDouble( header, 84, envelope.mmin );
        ByteUtils.writeLEDouble( header, 92, envelope.mmax );

        out.write( header, 0, 100 );
    }

    private void writeShapes( OutputStream mainOut, OutputStream indexOut )
                            throws IOException {
        // allocate the WHOLE shape file
        byte[] bytes = new byte[shapeFile.getSize()];
        byte[] indexBytes = new byte[8 * shapeFile.getShapes().size()];

        int recordNum = 1;
        int offset = 0;
        int indexOffset = 0;

        for ( Shape s : shapeFile.getShapes() ) {
            ByteUtils.writeBEInt( indexBytes, indexOffset, ( 100 + offset ) / 2 );
            indexOffset += 4;
            ByteUtils.writeBEInt( indexBytes, indexOffset, s.getByteLength() / 2 );
            indexOffset += 4;

            ByteUtils.writeBEInt( bytes, offset, recordNum++ );
            offset += 4;
            ByteUtils.writeBEInt( bytes, offset, s.getByteLength() / 2 ); // again 16-bit words
            offset += 4;

            LOG.logDebug( "Writing a " + s.getClass().getSimpleName() + ", size " + s.getByteLength() + " from offset "
                          + offset );
            offset = s.write( bytes, offset );
        }

        mainOut.write( bytes, 0, bytes.length );
        indexOut.write( indexBytes, 0, indexBytes.length );
    }

    /**
     * Writes the shapes to the files with the given base name.
     * 
     * @param baseName
     * @throws IOException
     * @throws DBaseException
     * @deprecated
     */
    @Deprecated
    public void write( String baseName )
                            throws IOException, DBaseException {
        LOG.logWarning( "Base name parameter " + baseName + " is ignored! Base name of " + shapeFile.getBaseName()
                        + " is used instead." );
        write();
    }

    /**
     * Writes the shapes to file.
     * 
     * @throws IOException
     * @throws DBaseException
     */
    public void write()
                            throws IOException, DBaseException {
        String baseName = shapeFile.getBaseName();
        File mainFile = new File( baseName + ".shp" );
        BufferedOutputStream mainOut = new BufferedOutputStream( new FileOutputStream( mainFile ) );
        writeHeader( mainOut, ( shapeFile.getSize() + 100 ) / 2 );

        File indexFile = new File( baseName + ".shx" );
        BufferedOutputStream indexOut = new BufferedOutputStream( new FileOutputStream( indexFile ) );
        writeHeader( indexOut, ( shapeFile.getShapes().size() * 8 + 100 ) / 2 );

        writeShapes( mainOut, indexOut );

        mainOut.close();
        indexOut.close();

        shapeFile.writeDBF();
    }

    public Pair<OutputStream, OutputStream> writeHeaders( int size, int num, int shapeType, ShapeEnvelope envelope )
                            throws IOException, DBaseException {
        File mainFile = new File( name + ".shp" );
        BufferedOutputStream mainOut = new BufferedOutputStream( new FileOutputStream( mainFile ) );
        writeHeader( mainOut, size / 2, shapeType, envelope );

        File indexFile = new File( name + ".shx" );
        BufferedOutputStream indexOut = new BufferedOutputStream( new FileOutputStream( indexFile ) );
        writeHeader( indexOut, ( num * 8 + 100 ) / 2, shapeType, envelope );
        offset = 100;
        recordNum = 1;
        return new Pair<OutputStream, OutputStream>( indexOut, mainOut );
    }

    public void writeShape( OutputStream indexOut, OutputStream mainOut, Shape shape )
                            throws IOException {
        byte[] bytes = new byte[shape.getByteLength() + 8];
        byte[] indexBytes = new byte[8];

        ByteUtils.writeBEInt( indexBytes, 0, ( 100 + offset ) / 2 );
        ByteUtils.writeBEInt( indexBytes, 4, shape.getByteLength() / 2 );
        indexOut.write( indexBytes );

        ByteUtils.writeBEInt( bytes, 0, recordNum++ );
        offset += 4;
        ByteUtils.writeBEInt( bytes, 4, shape.getByteLength() / 2 ); // again 16-bit words
        offset += 4;

        offset = shape.write( bytes, 8 );
        mainOut.write( bytes );

    }

}
