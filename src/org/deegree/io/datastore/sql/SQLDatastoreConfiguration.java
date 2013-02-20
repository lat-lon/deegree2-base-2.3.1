//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/sql/SQLDatastoreConfiguration.java $
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
 Aennchenstraße 19
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
package org.deegree.io.datastore.sql;

import org.deegree.io.JDBCConnection;
import org.deegree.io.datastore.DatastoreConfiguration;

/**
 * Represents the configuration for an SQL database which is used as a datastore backend.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Thu, 27 Dec 2007) $
 */
public class SQLDatastoreConfiguration implements DatastoreConfiguration {

    private JDBCConnection connection;

    private Class datastoreClass;

    /**
     * Creates a new instance of <code>SQLDatastoreConfiguration</code> from the given parameters.
     * 
     * @param connection
     * @param datastoreClass
     */
    public SQLDatastoreConfiguration( JDBCConnection connection, Class datastoreClass ) {
        this.connection = connection;
        this.datastoreClass = datastoreClass;
    }

    public Class getDatastoreClass() {
        return this.datastoreClass;
    }    
    
    /**
     * Returns the JDBC connection information.
     * 
     * @return the JDBC connection information
     */
    public JDBCConnection getJDBCConnection() {
        return this.connection;
    }

    @Override
    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append( SQLDatastoreConfiguration.class.getName() );
        sb.append( datastoreClass.getName() );
        sb.append( connection );
        return sb.toString().hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( !( obj instanceof SQLDatastoreConfiguration ) ) {
            return false;
        }
        SQLDatastoreConfiguration that = (SQLDatastoreConfiguration) obj;
        if ( !this.connection.equals( that.connection ) ) {
            return false;
        }
        return true;
    }
}