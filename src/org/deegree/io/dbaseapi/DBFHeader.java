//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/dbaseapi/DBFHeader.java $

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

package org.deegree.io.dbaseapi;

import org.deegree.model.spatialschema.ByteUtils;

/**
 * Class representing the header of a dBase III/IV file
 * 
 * @version 03.05.2000
 * @author Andreas Poth
 */
public class DBFHeader {

    private byte[] header = null;

    /**
     * constructor retrieves number of fields
     */
    public DBFHeader( FieldDescriptor[] fieldDesc ) throws DBaseException {

        try {
            // allocate memory for the header
            header = new byte[32 + fieldDesc.length * 32 + 1];

            // set file type
            header[0] = 3;

            // set date YYMMDD
            header[1] = 0;
            header[2] = 1;
            header[3] = 1;

            // number of records in the dBase file
            ByteUtils.writeLEInt( header, 4, 0 );

            // write number of bytes in the header
            ByteUtils.writeLEShort( header, 8, header.length );

            // set field descriptors and calculate length of the data section
            int sum = 0;
            for ( int i = 0; i < fieldDesc.length; i++ ) {
                setField( i, fieldDesc[i] );
                byte[] data = fieldDesc[i].getFieldDescriptor();
                sum += data[16];
                data = null;
            }

            sum++;

            // write number of bytes in the record (data section)
            ByteUtils.writeLEShort( header, 10, sum );

            // set field terminator
            header[header.length - 1] = 0x0D;
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new DBaseException( e.getMessage() );
        }

    }

    /**
     * method: public void setField(int index, FieldDescriptor fd) puts a field on the header byte
     * array
     */
    private void setField( int index, FieldDescriptor fd ) {

        // get field descriptor data
        byte[] fddata = fd.getFieldDescriptor();

        // put field descriptor data on header byte array
        for ( int i = 0; i < 32; i++ ) {
            header[32 + ( index * 32 ) + i] = fddata[i];
        }

    }

    /**
     * method: public byte[] getHeader() throws DBaseException returns the header as a byte array
     * 
     */
    public byte[] getHeader() {
        return header;
    }

}
