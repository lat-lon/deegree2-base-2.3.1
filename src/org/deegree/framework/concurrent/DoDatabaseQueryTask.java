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

package org.deegree.framework.concurrent;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.deegree.io.JDBCConnection;
import org.deegree.io.databaseloader.PostgisDataLoader;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.ogcwebservices.wfs.operation.FeatureResult;
import org.deegree.ogcwebservices.wms.configuration.DatabaseDataSource;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class DoDatabaseQueryTask implements Callable<Object> {

    private DatabaseDataSource datasource;

    private Envelope envelope;

    private String sql;

    /**
     * Uses the sql template from the data source.
     * 
     * @param datasource
     * @param envelope
     */
    public DoDatabaseQueryTask( DatabaseDataSource datasource, Envelope envelope ) {
        this.datasource = datasource;
        this.envelope = envelope;
    }

    /**
     * Uses the given sql template
     * 
     * @param datasource
     * @param envelope
     * @param sql
     *            custom sql template to use for fetching the data
     */
    public DoDatabaseQueryTask( DatabaseDataSource datasource, Envelope envelope, String sql ) {
        this.datasource = datasource;
        this.envelope = envelope;
        this.sql = sql;
    }

    public FeatureResult call()
                            throws Exception {
        JDBCConnection jdbc = datasource.getJDBCConnection();
        String driver = jdbc.getDriver();
        FeatureCollection fc = null;
        if ( driver.toUpperCase().indexOf( "POSTGRES" ) > -1 ) {
            fc = PostgisDataLoader.load( datasource, envelope, sql );
        } else if ( driver.toUpperCase().indexOf( "ORACLE" ) > -1 ) {
            Class<?> cls = Class.forName( "org.deegree.io.databaseloader.OracleDataLoader" );
            Method method = cls.getMethod( "load", DatabaseDataSource.class, Envelope.class );
            fc = (FeatureCollection) method.invoke( cls, datasource, envelope, sql );
            // fc = OracleDataLoader.load( datasource, envelope );
        } else {
            throw new Exception( "unsuported database type: " + driver );
        }
        FeatureResult result = new FeatureResult( null, fc );
        return result;
    }

}
