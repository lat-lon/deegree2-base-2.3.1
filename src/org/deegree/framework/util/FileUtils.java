//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/FileUtils.java $
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
package org.deegree.framework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.URL;

/**
 * the class offeres several static methods for handling file access
 * 
 * 
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */
public class FileUtils {

    /**
     * writes the the passed string to a file created using the passed file name. For writing to the
     * resource an <code>OutputStreamReader</code> with encoding read from
     * <code>CharsetUtils.getSystemCharset()</code>.
     * 
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static final void writeToFile( String fileName, String data )
                            throws IOException {
        writeToFile( fileName, data, CharsetUtils.getSystemCharset() );
    }

    /**
     * writes the the passed string to a file created using the passed file name using the defined
     * character encoding.
     * 
     * @param fileName
     * @param data
     * @param encoding
     * @throws IOException
     */
    public static final void writeToFile( String fileName, String data, String encoding )
                            throws IOException {
        FileOutputStream fos = new FileOutputStream( fileName );
        OutputStreamWriter osr = new OutputStreamWriter( fos, encoding );
        osr.write( data );
        osr.close();
    }

    /**
     * appends the passed string to the file identified by the passed name. If the file does not
     * exist an exception will be thrown.
     * 
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static final void appendsToFile( String fileName, String data )
                            throws IOException {
        File file = new File( fileName );
        if ( !file.exists() ) {
            throw new IOException( "file: " + fileName + " does not exist" );
        }
        RandomAccessFile raf = new RandomAccessFile( file, "rw" );
        raf.seek( raf.length() );
        raf.writeChars( data );
        raf.close();
    }

    /**
     * reads a Text file from its resource. For accessing the resource an
     * <code>InputStreamReader</code> with encoding read from
     * <code>CharsetUtils.getSystemCharset()</code>
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static StringBuffer readTextFile( File file )
                            throws IOException {
        return readTextFile( file.toURL() );
    }

    /**
     * reads a Text file from its resource. For accessing the resource an
     * <code>InputStreamReader</code> with encoding read from
     * <code>CharsetUtils.getSystemCharset()</code>
     * 
     * @param url
     * @return
     * @throws IOException
     */
    public static StringBuffer readTextFile( URL url )
                            throws IOException {
        return readTextFile( url.openStream() );
    }

    /**
     * reads a Text file from its resource. For accessing the resource an
     * <code>InputStreamReader</code> with encoding read from
     * <code>CharsetUtils.getSystemCharset()</code>
     * 
     * @param is
     * @return
     * @throws IOException
     */
    public static StringBuffer readTextFile( InputStream is )
                            throws IOException {
        InputStreamReader isr = new InputStreamReader( is, CharsetUtils.getSystemCharset() );
        return readTextFile( isr );
    }

    /**
     * reads a Text file from its resource.
     * 
     * @param reader
     * @return
     * @throws IOException
     */
    public static StringBuffer readTextFile( Reader reader )
                            throws IOException {
        StringBuffer sb = new StringBuffer( 10000 );
        int c = 0;
        while ( ( c = reader.read() ) > -1 ) {
            sb.append( (char) c );
        }
        reader.close();

        return sb;
    }

}
