//$HeadURL$
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

package org.deegree.ogcwebservices.wcs.configuration;

import org.deegree.io.JDBCConnection;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class DatabaseResolution extends AbstractResolution {

    private JDBCConnection jdbc;

    private String table;

    private String rootDir;

    /**
     * 
     * @param minScale
     * @param maxScale
     * @param ranges
     * @param jdbc
     * @param table
     */
    public DatabaseResolution( double minScale, double maxScale, Range[] ranges, JDBCConnection jdbc, String table,
                               String rootDir ) {
        super( minScale, maxScale, ranges );
        this.jdbc = jdbc;
        this.table = table;
        this.rootDir = rootDir;
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
     * @return the rootDir
     */
    public String getRootDir() {
        return rootDir;
    }

    /**
     * @param rootDir
     *            the rootDir to set
     */
    public void setRootDir( String rootDir ) {
        this.rootDir = rootDir;
    }

}
