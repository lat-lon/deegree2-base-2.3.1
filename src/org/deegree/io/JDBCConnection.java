//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/JDBCConnection.java $
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
package org.deegree.io;

/**
 * Class representation for an element of type "deegreejdbc:JDBCConnectionType" as defined in
 * datastore_configuration.xsd.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9342 $, $Date: 2007-12-27 13:32:57 +0100 (Thu, 27 Dec 2007) $
 * 
 * @TODO Change the type name!
 */
public class JDBCConnection {

    private String driver;

    private String url;

    private String user;

    private String password;

    private String securityConstraints;

    private String encoding;

    private String aliasPrefix;

    private String sdeDatabase;

    private String sdeVersion;

    /**
     * 
     * @param driver
     *            JDBC driver
     * @param url
     *            JDBC connection string
     * @param user
     *            user name
     * @param password
     *            users password
     * @param securityConstraints
     *            constraints to consider (not implemented yet)
     * @param encoding
     *            encoding to be used for connection
     * @param aliasPrefix ?
     */
    public JDBCConnection( String driver, String url, String user, String password, String securityConstraints,
                           String encoding, String aliasPrefix ) {
        this( driver, url, user, password, securityConstraints, encoding, aliasPrefix, (String) null, (String) null );
    }

    /**
     * 
     * @param driver
     *            JDBC driver
     * @param url
     *            JDBC connection string
     * @param user
     *            user name
     * @param password
     *            users password
     * @param securityConstraints
     *            constraints to consider (not implemented yet)
     * @param encoding
     *            encoding to be used for connection
     * @param aliasPrefix ?
     * @param sdeDatabase
     * @param sdeVersion
     */
    public JDBCConnection( String driver, String url, String user, String password, String securityConstraints,
                           String encoding, String aliasPrefix, String sdeDatabase, String sdeVersion ) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.securityConstraints = securityConstraints;
        this.encoding = encoding;
        this.aliasPrefix = aliasPrefix;
        this.sdeDatabase = sdeDatabase;
        this.sdeVersion = sdeVersion;
    }

    /**
     * @return driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @return encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @return aliasPrefix
     */
    public String getAliasPrefix() {
        return aliasPrefix;
    }

    /**
     * @return url
     */
    public String getURL() {
        return url;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return securityConstraints
     */
    public String getSecurityConstraints() {
        return securityConstraints;
    }

    /**
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return sdeDatabase
     */
    public String getSDEDatabase() {
        return sdeDatabase;
    }

    /**
     * @return sdeVersion
     */
    public String getSDEVersion() {
        return sdeVersion;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @param o
     *            the reference object with which to compare
     * @return <code>true</code> if this object is the same as the obj argument; false otherwise
     */
    public boolean equals( Object o ) {
        if ( !( o instanceof JDBCConnection ) ) {
            return false;
        }
        JDBCConnection that = (JDBCConnection) o;
        if ( !this.driver.equals( that.driver ) ) {
            return false;
        }
        if ( !this.url.equals( that.url ) ) {
            return false;
        }
        if ( !this.user.equals( that.user ) ) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append( "Driver: '" );
        sb.append( this.driver );
        sb.append( "', URL: '" );
        sb.append( this.url );
        sb.append( "', User: '" );
        sb.append( this.user );
        return sb.toString();
    }
}