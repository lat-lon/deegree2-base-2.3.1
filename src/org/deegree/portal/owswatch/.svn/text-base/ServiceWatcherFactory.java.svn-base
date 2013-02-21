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

package org.deegree.portal.owswatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.deegree.framework.util.StringTools;
import org.deegree.portal.owswatch.configs.GeneralConfig;
import org.deegree.portal.owswatch.configs.OwsWatchConfig;
import org.deegree.portal.owswatch.configs.OwsWatchConfigFactory;
import org.deegree.portal.owswatch.configs.User;

/**
 * Instantiates an instance of ServiceWatcher with instances of ServiceConfiguration and ServiceLog inside as parsed
 * from the configurations file
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:52 $
 */
public class ServiceWatcherFactory implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7205941737006048817L;

    private static String confFilePath = null;

    private static String webinfPath = null;

    private static ServicesConfigurationFactory servicesParser = null;

    private static OwsWatchConfig conf = null;

    private static ServiceWatcher watcher = null;

    private static ServiceWatcherFactory factory = null;

    private static String protDirPath = null;

    private static EmailSender sender = null;

    private static String servletAddr = null;

    private ServiceWatcherFactory() {

    }

    /**
     * @return ServiceWatcher
     * @throws ConfigurationsException
     */
    public ServiceWatcher getServiceWatcherInstance()
                            throws ConfigurationsException {
        return watcher;
    }

    /**
     * @param confFilePath
     * @param webinfPath
     * @return An instance of ServiceWatchFactory using singletone pattern
     * @throws ConfigurationsException
     */
    public static ServiceWatcherFactory getInstance( String confFilePath, String webinfPath )
                            throws ConfigurationsException {
        if ( factory != null ) {
            return factory;
        }
        factory = new ServiceWatcherFactory();
        ServiceWatcherFactory.confFilePath = confFilePath;
        ServiceWatcherFactory.webinfPath = webinfPath;
        parse();
        return factory;
    }

    /**
     * @return ServiceWatcher
     * @throws ConfigurationsException
     */
    private static ServiceWatcher parse()
                            throws ConfigurationsException {
        try {
            conf = OwsWatchConfigFactory.createOwsWatchConfig( confFilePath, webinfPath );
        } catch ( Exception e ) {
            throw new ConfigurationsException( e.getLocalizedMessage() );
        }

        servicesParser = new ServicesConfigurationFactory();
        List<ServiceConfiguration> services = parseServiceConfigurations( servicesParser, conf, webinfPath );

        watcher = createServiceWatcher( conf, services );
        return watcher;
    }

    private static ServiceWatcher createServiceWatcher( OwsWatchConfig conf, List<ServiceConfiguration> services )
                            throws ConfigurationsException {

        servletAddr = conf.getGeneral().getServerAddress();
        servletAddr = StringTools.concat( 100, servletAddr, !servletAddr.endsWith( "/" ) ? "/" : "", "wprotocol" );
        StringBuilder builder = new StringBuilder( webinfPath );
        protDirPath = builder.append( conf.getGeneral().getProtFolderPath() ).toString();

        ServiceWatcher watcher = new ServiceWatcher();
        GeneralConfig general = conf.getGeneral();

        sender = new EmailSender( general.getMailFrom(), general.getMailServer(), getUserEmails( general.getUsers() ) );

        Iterator<ServiceConfiguration> it = services.iterator();
        while ( it.hasNext() ) {
            ServiceConfiguration service = it.next();
            try {
                ServiceLog serviceLog = new ServiceLog( protDirPath, service.getServiceid(), service.getServiceName(),
                                                        service.getServiceType(), servletAddr, sender );
                watcher.addService( service, serviceLog );
            } catch ( Exception e ) {
                throw new ConfigurationsException( e.getLocalizedMessage() );
            }
        }
        return watcher;
    }

    /**
     * builds a file path from the given webinfPath and the services instances path located in OwsWatchconfig. after
     * calling this function, other values from the parser could be extracted like the prefix and the idSequence
     * 
     * @param servicesParser
     * @param conf
     * @param webinfPath
     * @return List of ServiceConfiguration
     */
    private static List<ServiceConfiguration> parseServiceConfigurations( ServicesConfigurationFactory servicesParser,
                                                                          OwsWatchConfig conf, String webinfPath )
                            throws ConfigurationsException {
        String serviceAddr = conf.getGeneral().getServerAddress();
        serviceAddr = StringTools.concat( 100, serviceAddr, !serviceAddr.endsWith( "/" ) ? "/" : "", "wprotocol" );

        StringBuilder builder = new StringBuilder( webinfPath );
        String servicesUrl = builder.toString() + conf.getGeneral().getServiceInstancesPath();
        try {
            return servicesParser.parseServices( servicesUrl );
        } catch ( Exception e ) {
            throw new ConfigurationsException( e.getLocalizedMessage() );
        }
    }

    /**
     * @return Path of the configuration file
     */
    public String getConfFilePath() {
        return confFilePath;
    }

    /**
     * @return WEB-INF folder path
     */
    public String getWebinfPath() {
        return webinfPath;
    }

    /**
     * @return {@link OwsWatchConfigFactory}
     */
    public OwsWatchConfig getConf() {
        return conf;
    }

    /**
     * @return {@link ServicesConfigurationFactory}
     */
    public ServicesConfigurationFactory getServicesParser() {
        return servicesParser;
    }

    protected static List<String> getUserEmails( Map<String, User> users ) {
        // Adding users emails
        Iterator<String> it = users.keySet().iterator();
        List<String> list = new ArrayList<String>();
        while ( it.hasNext() ) {
            User user = users.get( it.next() );
            list.add( user.getEmail() );
        }
        return list;
    }

    /**
     * @return Protocol directory path
     */
    public String getProtDirPath() {
        return protDirPath;
    }

    /**
     * @return EmailSender
     */
    public EmailSender getSender() {
        return sender;
    }

    /**
     * @return String
     */
    public String getServletAddr() {
        return servletAddr;
    }
}
