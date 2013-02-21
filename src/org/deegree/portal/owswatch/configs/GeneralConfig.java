//$$Header: $$
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

package org.deegree.portal.owswatch.configs;

import java.io.Serializable;
import java.util.Map;

/**
 * GeneralConfiguration Element of the owswatchConfiguration.xml
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:52 $
 */
public class GeneralConfig implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4300517867661849988L;

    private int globalRefreshRate;

    private Map<String, User> users = null;

    private String mailServer = null;

    private String mailFrom = null;

    private String protFolderPath = null;

    private String serviceInstancesPath = null;

    private String serverAddress = null;

    /**
     * @param globalRefreshRate
     * @param users
     * @param mailFrom
     * @param mailServer
     * @param protFolderPath
     * @param serviceInstancesPath
     * @param serverAddress
     * 
     */
    public GeneralConfig( int globalRefreshRate, Map<String, User> users, String mailFrom, String mailServer,
                          String protFolderPath, String serviceInstancesPath, String serverAddress ) {

        this.globalRefreshRate = globalRefreshRate;
        this.users = users;
        this.mailFrom = mailFrom;
        this.mailServer = mailServer;
        this.protFolderPath = protFolderPath;
        this.serviceInstancesPath = serviceInstancesPath;
        this.serverAddress = serverAddress;
    }

    /**
     * @return GlobalRefreshRate
     */
    public int getGlobalRefreshRate() {
        return globalRefreshRate;
    }

    /**
     * @param globalRefreshRate
     */
    public void setGlobalRefreshRate( int globalRefreshRate ) {
        this.globalRefreshRate = globalRefreshRate;
    }

    /**
     * @return The email sender
     */
    public String getMailFrom() {
        return mailFrom;
    }

    /**
     * @param mailFrom
     *            The email sender
     */
    public void setMailFrom( String mailFrom ) {
        this.mailFrom = mailFrom;
    }

    /**
     * @return MailServer
     */
    public String getMailServer() {
        return mailServer;
    }

    /**
     * @param mailServer
     */
    public void setMailServer( String mailServer ) {
        this.mailServer = mailServer;
    }

    /**
     * @return Path to the protocol folder relativ to WEB-INF/conf/owswatch
     */
    public String getProtFolderPath() {
        return protFolderPath;
    }

    /**
     * @param protFolderPath
     *            Path to the protocol folder relativ to WEB-INF/conf/owswatch
     */
    public void setProtFolder( String protFolderPath ) {
        this.protFolderPath = protFolderPath;
    }

    /**
     * @return www Address of owsWatch
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * @param serverAddress
     *            www Address of owsWatch
     */
    public void setServerAddress( String serverAddress ) {
        this.serverAddress = serverAddress;
    }

    /**
     * @return path to serviceInstances xml relativ to WEB-INF/conf/owswatch
     */
    public String getServiceInstancesPath() {
        return serviceInstancesPath;
    }

    /**
     * @param serviceInstancesPath
     *            path to serviceInstances xml relativ to WEB-INF/conf/owswatch
     */
    public void setServiceInstancesPath( String serviceInstancesPath ) {
        this.serviceInstancesPath = serviceInstancesPath;
    }

    /**
     * @return Users
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * @param users
     *            Users
     */
    public void setUsers( Map<String, User> users ) {
        this.users = users;
    }
}
