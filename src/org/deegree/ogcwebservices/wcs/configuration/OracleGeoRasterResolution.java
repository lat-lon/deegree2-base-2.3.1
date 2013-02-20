// $HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/configuration/OracleGeoRasterResolution.java $
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
package org.deegree.ogcwebservices.wcs.configuration;

import org.deegree.io.JDBCConnection;

/**
 * models a <tt>Resolution<tT> by describing the assigned coverages through
 * a Oracle 10g Georaster 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10679 $, $Date: 2008-03-25 18:26:57 +0100 (Tue, 25 Mar 2008) $
 */

public class OracleGeoRasterResolution extends AbstractResolution {

    private JDBCConnection jdbc = null;

    private String table = null;

    private String rdtTable = null;

    private String column = null;

    private String identification = null;

    private int level = 1;

    /**
     * @param minScale
     * @param maxScale
     * @param ranges
     * @param jdbc
     *            descrition of the database connection
     * @param table
     * @param rdtTable
     * @param column
     * @param identification
     * @param level
     * @throws IllegalArgumentException
     */
    public OracleGeoRasterResolution( double minScale, double maxScale, Range[] ranges, JDBCConnection jdbc,
                                      String table, String rdtTable, String column, String identification, int level )
                            throws IllegalArgumentException {
        super( minScale, maxScale, ranges );
        this.jdbc = jdbc;
        this.table = table;
        this.column = column;
        this.rdtTable = rdtTable;
        this.identification = identification;
        this.level = level;

    }

    /**
     * @return Returns the shape.
     */
    public JDBCConnection getJDBCConnection() {
        return jdbc;
    }

    /**
     * @param jdbc
     * @param shape
     *            The shape to set.
     */
    public void setJDBCConnection( JDBCConnection jdbc ) {
        this.jdbc = jdbc;
    }

    /**
     * returns the name of the table storeing the raster data
     * 
     * @return the name of the table storeing the raster data
     */
    public String getTable() {
        return table;
    }

    /**
     * @see #getTable()
     * @param table
     */
    public void setTable( String table ) {
        this.table = table;
    }

    /**
     * returns the name of the assigned GeoRaster column of the table
     * 
     * @return the name of the assigned GeoRaster column of the table
     */
    public String getColumn() {
        return column;
    }

    /**
     * @see #getColumn()
     * @param column
     */
    public void setColumn( String column ) {
        this.column = column;
    }

    /**
     * returns a SQL where condition to identify the table row/raster instance to access
     * 
     * @return a SQL where condition to identify the table row/raster instance to access
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * @see #getIdentification()
     * @param identification
     */
    public void setIdentification( String identification ) {
        this.identification = identification;
    }

    /**
     * returns the name of the RDT Table assigned to the GetRaster column
     * 
     * @return the name of the RDT Table assigned to the GetRaster column
     */
    public String getRdtTable() {
        return rdtTable;
    }

    /**
     * @see #getRdtTable()
     * @param rdtTable
     */
    public void setRdtTable( String rdtTable ) {
        this.rdtTable = rdtTable;
    }

    /**
     * returns the raster level assigned to a resolution instance
     * 
     * @return the raster level assigned to a resolution instance
     */
    public int getLevel() {
        return level;
    }

    /**
     * @see #getLevel()
     * @param level
     */
    public void setLevel( int level ) {
        this.level = level;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer( 500 );
        sb.append( getClass().getName() ).append( ":\n" );
        sb.append( "JDBCConnection: " ).append( jdbc ).append( "\n" );
        sb.append( "table: " ).append( table ).append( "\n" );
        sb.append( "rdttable: " ).append( rdtTable ).append( "\n" );
        sb.append( "column: " ).append( column ).append( "\n" );
        sb.append( "identification: " ).append( identification ).append( "\n" );
        sb.append( "level: " ).append( level );
        return sb.toString();
    }

}