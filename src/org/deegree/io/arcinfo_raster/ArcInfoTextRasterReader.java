//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/arcinfo_raster/ArcInfoTextRasterReader.java $
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
package org.deegree.io.arcinfo_raster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.deegree.framework.util.StringTools;
import org.deegree.model.coverage.grid.WorldFile;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;

/**
 * reads a raster in ArcInfo text format:<br>
 * 
 * <pre>
 *  ncols         1600
 *  nrows         1600
 *  xllcorner     3540000
 *  yllcorner     5730000
 *  cellsize      25
 *  NODATA_value  -9999
 *  120.4 132.5 99.9 ... 98.32
 *  122.5 111.6 110.9 ... 88.77
 *  ...
 *  234.23 233.4 265.9 ... 334.7
 * </pre>
 * 
 * @version $Revision: 12144 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: otonnhofer $
 * 
 * @version 1.0. $Revision: 12144 $, $Date: 2008-06-04 11:48:17 +0200 (Mi, 04. Jun 2008) $
 * 
 * @since 2.0
 */
public class ArcInfoTextRasterReader {

    private File file;

    private WorldFile wf;

    private int rows = 0;

    private int cols = 0;

    private double minx = 0;

    private double miny = 0;

    private double res = 0;

    private double nodata = 0;

    /**
     * @param file
     */
    public ArcInfoTextRasterReader( File file ) {
        this.file = file;
    }

    /**
     * @param fileName
     */
    public ArcInfoTextRasterReader( String fileName ) {
        this.file = new File( fileName );
    }

    /**
     * reads metadata from a ArcInfo Grid text file
     * 
     * @return worldfile for the text grid file
     * @throws IOException
     */
    public WorldFile readMetadata()
                            throws IOException {

        if ( wf == null ) {
            BufferedReader br = new BufferedReader( new FileReader( file ) );

            // number of raster columns
            String line = br.readLine();
            String[] tmp = StringTools.toArray( line, " \t", false );
            cols = Integer.parseInt( tmp[1] );

            // number of raster rows
            line = br.readLine();
            tmp = StringTools.toArray( line, " \t", false );
            rows = Integer.parseInt( tmp[1] );

            // x-coordinate of left lower corner
            line = br.readLine();
            String[] cornerx = StringTools.toArray( line, " \t", false );
            minx = Double.parseDouble( cornerx[1].replace( ',', '.' ) );

            // y-coordinate of left lower corner
            line = br.readLine();
            String[] cornery = StringTools.toArray( line, " \t", false );
            miny = Double.parseDouble( cornery[1].replace( ',', '.' ) );

            // raster resolution
            line = br.readLine();
            tmp = StringTools.toArray( line, " \t", false );
            res = Double.parseDouble( tmp[1].replace( ',', '.' ) );

            // raster resolution
            line = br.readLine();
            tmp = StringTools.toArray( line, " \t", false );
            nodata = Double.parseDouble( tmp[1].replace( ',', '.' ) );

            if ( cornerx[0].toUpperCase().indexOf( "XLLCORNER" ) > -1 ) {
                minx = minx + res / 2d;
            }
            if ( cornery[0].toUpperCase().indexOf( "YLLCORNER" ) > -1 ) {
                miny = miny + res / 2d;
            }

            br.close();

            Envelope env = GeometryFactory.createEnvelope( minx, miny, minx + res * ( cols - 1 ), miny + res
                                                                                                  * ( rows - 1 ), null );
            wf = new WorldFile( res, res, 0, 0, env );

        }

        return wf;
    }

    /**
     * returns the value used by a ArcInfo grid to indicate no data values
     * 
     * @return the nodata value
     * @throws IOException
     */
    public double getNoDataValue()
                            throws IOException {

        // ensure that metadata has been read
        readMetadata();

        return nodata;
    }

    /**
     * reads data from a ArcInfo Grid text file
     * 
     * @return the grid
     * @throws IOException
     */
    public float[][] readData()
                            throws IOException {

        // ensure that metadata has been read
        readMetadata();

        BufferedReader br = new BufferedReader( new FileReader( file ) );
        for ( int i = 0; i < 6; i++ ) {
            // skip first six rows containing metadata
            br.readLine();
        }

        float[][] data = new float[rows][];
        for ( int i = 0; i < data.length; i++ ) {
            data[i] = StringTools.toArrayFloat( br.readLine(), " \t" );
        }
        br.close();

        return data;
    }

}
