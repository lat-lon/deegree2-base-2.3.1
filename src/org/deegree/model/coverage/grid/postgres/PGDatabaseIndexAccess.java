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

package org.deegree.model.coverage.grid.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.StringTools;
import org.deegree.io.DBConnectionPool;
import org.deegree.io.JDBCConnection;
import org.deegree.io.datastore.sql.postgis.PGgeometryAdapter;
import org.deegree.model.coverage.grid.DatabaseIndexAccess;
import org.deegree.model.coverage.grid.DatabaseIndexedGCMetadata;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.ogcwebservices.wcs.configuration.File;
import org.postgis.PGgeometry;
import org.postgresql.PGConnection;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class PGDatabaseIndexAccess implements DatabaseIndexAccess {

    private static final ILogger LOG = LoggerFactory.getLogger( PGDatabaseIndexAccess.class );

    private static final String GEOMETRY_DATATYPE_NAME = "geometry";

    private static final String PG_GEOMETRY_CLASS_NAME = "org.postgis.PGgeometry";

    private static Class pgGeometryClass;

    static {
        try {
            pgGeometryClass = Class.forName( PG_GEOMETRY_CLASS_NAME );
        } catch ( ClassNotFoundException e ) {
            LOG.logError( "Cannot find class '" + PG_GEOMETRY_CLASS_NAME + "'.", e );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.model.coverage.grid.DatabaseIndexAccess#getFiles(org.deegree.model.coverage.grid.DatabaseIndexedGCMetadata,
     *      org.deegree.model.spatialschema.Envelope, org.deegree.model.crs.CoordinateSystem)
     */
    public File[] getFiles( DatabaseIndexedGCMetadata dbigcmd, Envelope envelope, CoordinateSystem crs ) {
        JDBCConnection jdbc = dbigcmd.getJDBCConnection();
        DBConnectionPool pool = DBConnectionPool.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = pool.acquireConnection( jdbc.getDriver(), jdbc.getURL(), jdbc.getUser(), jdbc.getPassword() );
            PGConnection pgConn = (PGConnection) con;
            pgConn.addDataType( GEOMETRY_DATATYPE_NAME, pgGeometryClass );

            // get level number
            String sql = StringTools.concat( 300, "select level from ", dbigcmd.getTable(),
                                             "_pyr where minscale < ? AND maxscale >= ?" );
            LOG.logDebug( sql );
            LOG.logDebug( "scale ", dbigcmd.getScale() );
            stmt = con.prepareStatement( sql );
            stmt.setFloat( 1, dbigcmd.getScale() );
            stmt.setFloat( 2, dbigcmd.getScale() );
            ResultSet rs = stmt.executeQuery();
            if ( !rs.next() ) {
                // no tiles/level defined for current scale
                rs.close();
                return new File[0];
            }
            int level = rs.getInt( 1 );
            rs.close();
            stmt.close();
            // get file names (postgis version)
            sql = StringTools.concat( 300, "select DIR, FILE, BBOX  from ", dbigcmd.getTable(),
                                      " where level = ? AND BBOX && ?" );

            stmt = con.prepareStatement( sql );
            stmt.setInt( 1, level );
            Geometry geom = GeometryFactory.createSurface( envelope, null );
            stmt.setObject( 2, PGgeometryAdapter.export( geom, -1 ) );
            LOG.logDebug( sql );
            LOG.logDebug( "level/bbox ", level + " " + envelope );
            rs = stmt.executeQuery();
            List<File> list = new ArrayList<File>( 50 );
            while ( rs.next() ) {
                String dir = rs.getString( 1 );
                String file = rs.getString( 2 );
                Envelope env = PGgeometryAdapter.wrap( (PGgeometry) rs.getObject( 3 ), crs ).getEnvelope();
                String fn = StringTools.concat( 256, dbigcmd.getRootDir(), '/', dir, '/', file );
                list.add( new File( crs, fn, env ) );
            }
            LOG.logInfo( "tiles found: ", list.size() );
            rs.close();
            return list.toArray( new File[list.size()] );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch ( Exception e ) {
                LOG.logWarning( "could not close sql statement" );
            }
            try {
                pool.releaseConnection( con, jdbc.getDriver(), jdbc.getURL(), jdbc.getUser(), jdbc.getPassword() );
            } catch ( Exception e ) {
                LOG.logWarning( "could not release sql connection back to pool" );
            }
        }
        return new File[0];
    }
}
