//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/OGCServletController.java $
// $Id: OGCServletController.java 10660 2008-03-24 21:39:54Z apoth $
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

package org.deegree.enterprise.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.enterprise.AbstractOGCServlet;
import org.deegree.enterprise.ServiceException;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.KVP2Map;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.util.WebappResourceResolver;
import org.deegree.framework.version.Version;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.ogcwebservices.ExceptionReport;
import org.deegree.ogcwebservices.OGCRequestFactory;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.wmps.configuration.WMPSConfigurationDocument;
import org.deegree.ogcwebservices.wms.configuration.WMSConfigurationDocument;
import org.deegree.ogcwebservices.wms.configuration.WMSConfigurationDocument_1_3_0;
import org.deegree.owscommon.XMLFactory;
import org.xml.sax.SAXException;

/**
 * An <code>OGCServletController</code> handles all incoming requests. The controller for all OGC
 * service requests. Dispatcher to specific handler for WMS, WFS and other.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @see <a
 *      href="http://java.sun.com/blueprints/corej2eepatterns/Patterns/FrontController.html">Front
 *      controller </a>
 */
public class OGCServletController extends AbstractOGCServlet {

    /**
     * address is the url of the client which requests.
     */
    public static String address = null;

    private static final long serialVersionUID = -4461759017823581221L;

    private static final ILogger LOG = LoggerFactory.getLogger( OGCServletController.class );

    private static final String SERVICE = "services";

    private static final String HANDLER_CLASS = ".handler";

    private static final String HANDLER_CONF = ".config";

    private static final Map<Class<?>, String> SERVICE_FACTORIES_MAPPINGS = new HashMap<Class<?>, String>();

    private static final String ERR_MSG = "Can't set configuration for {0}";

    /**
     * 
     * 
     * @param request
     * @param response
     * @TODO refactor and optimize code for initializing handler
     */
    public void doService( HttpServletRequest request, HttpServletResponse response ) {
        if ( response.isCommitted() ) {
            LOG.logWarning( "The response object is already committed, cannot proceed!" );
            return;
        }

        long startTime = System.currentTimeMillis();
        address = request.getRequestURL().toString();

        String service = null;
        try {
            OGCWebServiceRequest ogcRequest = OGCRequestFactory.create( request );

            LOG.logInfo( StringTools.concat( 500, "Handling request '", ogcRequest.getId(), "' from '",
                                             request.getRemoteAddr(), "' to service: '", ogcRequest.getServiceName(),
                                             "'" ) );

            // get service from request
            service = ogcRequest.getServiceName().toUpperCase();

            // get handler instance
            ServiceDispatcher handler = ServiceLookup.getInstance().getHandler( service, request.getRemoteAddr() );
            // dispatch request to specific handler
            handler.perform( ogcRequest, response );
        } catch ( OGCWebServiceException e ) {
            LOG.logError( e.getMessage(), e );
            sendException( response, e, request, service );
        } catch ( ServiceException e ) {
            if ( e.getNestedException() instanceof OGCWebServiceException ) {
                sendException( response, (OGCWebServiceException) e.getNestedException(), request, service );
            } else {
                sendException( response, new OGCWebServiceException( this.getClass().getName(), e.getMessage() ),
                               request, service );
            }
            LOG.logError( e.getMessage(), e );
        } catch ( Exception e ) {
            sendException( response, new OGCWebServiceException( this.getClass().getName(), e.getMessage() ), request,
                           service );
            LOG.logError( e.getMessage(), e );
        }
        if ( LOG.isDebug() ) {
            LOG.logDebug( "OGCServletController: request performed in "
                          + Long.toString( System.currentTimeMillis() - startTime ) + " milliseconds." );
        }
    }

    /**
     * Sends the passed <code>OGCWebServiceException</code> to the calling client.
     * 
     * @param response
     * @param e
     */
    @SuppressWarnings("unchecked")
    // for castthe map
    private void sendException( HttpServletResponse response, OGCWebServiceException e, HttpServletRequest request,
                                String service ) {
        LOG.logInfo( "Sending OGCWebServiceException to client." );

        // according to Sun's JavaDoc, the map always has this type
        Map<String, String[]> map = request.getParameterMap();

        boolean isWMS130 = false, isCSW = false, isWCTS = false;

        if ( service != null ) {
            if ( "wms".equalsIgnoreCase( service ) ) {
                for ( String str : map.keySet() ) {
                    if ( str.equalsIgnoreCase( "version" ) ) {
                        String[] version = map.get( str );
                        if ( version != null && version.length > 0 && version[0].equals( "1.3.0" ) ) {
                            isWMS130 = true;
                        }
                    }
                }
            }

            isCSW = "csw".equalsIgnoreCase( service );
            isWCTS = "wcts".equalsIgnoreCase( service );
        } else {
            // could clash with other services!
            for ( String str : map.keySet() ) {
                if ( str.equalsIgnoreCase( "version" ) ) {
                    String[] version = map.get( str );
                    if ( version != null && version.length > 0 && version[0].equals( "1.3.0" ) ) {
                        isWMS130 = true;
                    }
                }
            }

            for ( String str : map.keySet() ) {
                if ( str.equalsIgnoreCase( "service" ) ) {
                    isCSW = map.get( str )[0].equalsIgnoreCase( "csw" );
                    isWCTS = "wcts".equalsIgnoreCase( map.get( str )[0] );
                    break;
                }
            }

            try {
                XMLFragment doc = new XMLFragment( request.getReader(), XMLFragment.DEFAULT_URL );
                service = OGCRequestFactory.getTargetService( "", "", doc.getRootElement().getOwnerDocument() );
                isCSW = "csw".equalsIgnoreCase( service );
                isWCTS = "wcts".equalsIgnoreCase( service );
            } catch ( SAXException e1 ) {
                // ignore
            } catch ( IOException e1 ) {
                // ignore
            }
        }

        try {
            XMLFragment doc;
            String contentType = "text/xml";
            if ( isWMS130 || "wcs".equalsIgnoreCase( e.getLocator() ) ) {
                doc = XMLFactory.exportNS( new ExceptionReport( new OGCWebServiceException[] { e } ) );
            } else if ( isCSW ) {
                doc = XMLFactory.exportExceptionReport( new ExceptionReport( new OGCWebServiceException[] { e } ) );
            } else if ( isWCTS ) {
                doc = org.deegree.owscommon_1_1_0.XMLFactory.exportException( e );
            } else {
                contentType = "application/vnd.ogc.se_xml";
                doc = XMLFactory.export( new ExceptionReport( new OGCWebServiceException[] { e } ) );
            }
            response.setContentType( contentType );
            OutputStream os = response.getOutputStream();
            doc.write( os );
            os.close();
        } catch ( Exception ex ) {
            LOG.logError( "ERROR: " + ex.getMessage(), ex );
        }
    }

    /**
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
                            throws ServletException, IOException {

        LOG.logDebug( "query string ", request.getQueryString() );
        if ( request.getParameter( "RELOADDEEGREE" ) != null ) {
            reloadServices( request, response );
        } else {
            this.doService( request, response );
        }
    }

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void reloadServices( HttpServletRequest request, HttpServletResponse response )
                            throws ServletException, IOException {
        Map<?, ?> map = KVP2Map.toMap( request );
        String user = (String) map.get( "USER" );
        String password = (String) map.get( "PASSWORD" );
        String message = null;
        if ( getInitParameter( "USER" ) != null && getInitParameter( "PASSWORD" ) != null
             && getInitParameter( "USER" ).equals( user ) && getInitParameter( "PASSWORD" ).equals( password ) ) {
            initServices( getServletContext() );
            ctDestroyed();
            message = Messages.getString( "OGCServletController.reloadsuccess" );
        } else {
            message = Messages.getString( "OGCServletController.reloadfailed" );
        }
        PrintWriter pw = response.getWriter();
        pw.print( message );
        pw.flush();
        pw.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
                            throws ServletException, IOException {
        this.doService( request, response );
    }

    /**
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init()
                            throws ServletException {
        super.init();

        LOG.logDebug( "Logger for " + this.getClass().getName() + " initialized." );

        SERVICE_FACTORIES_MAPPINGS.put( CSWHandler.class, "org.deegree.ogcwebservices.csw.CSWFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WFSHandler.class, "org.deegree.ogcwebservices.wfs.WFServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WCSHandler.class, "org.deegree.ogcwebservices.wcs.WCServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WMSHandler.class, "org.deegree.ogcwebservices.wms.WMServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( SOSHandler.class, "org.deegree.ogcwebservices.sos.SOServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WPVSHandler.class, "org.deegree.ogcwebservices.wpvs.WPVServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WMPSHandler.class, "org.deegree.ogcwebservices.wmps.WMPServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WPSHandler.class, "org.deegree.ogcwebservices.wps.WPServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WASSHandler.class, "org.deegree.ogcwebservices.wass.common.WASServiceFactory" );
        SERVICE_FACTORIES_MAPPINGS.put( WCTSHandler.class, "org.deegree.ogcwebservices.wcts.WCTServiceFactory" );

        LOG.logInfo( "-------------------------------------------------------------------------------" );
        LOG.logInfo( "Starting deegree version " + Version.getVersion() );
        LOG.logInfo( "- context        : " + this.getServletContext().getServletContextName() );
        LOG.logInfo( "- real path      : " + this.getServletContext().getRealPath( "/" ) );
        LOG.logInfo( "- java version   : " + System.getProperty( "java.version" ) + "" );
        LOG.logInfo( "- system charset : " + CharsetUtils.getSystemCharset() );
        LOG.logInfo( "- default charset: " + Charset.defaultCharset() );
        LOG.logInfo( "- server info    : " + this.getServletContext().getServerInfo() );
        try {
            LOG.logInfo( "- ip            : " + InetAddress.getLocalHost().getHostAddress() );
            LOG.logInfo( "- host name     : " + InetAddress.getLocalHost().getHostName() );
            LOG.logInfo( "- domain name   : " + InetAddress.getLocalHost().getCanonicalHostName() );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
        }
        LOG.logInfo( "-------------------------------------------------------------------------------" );
        this.initServices( getServletContext() );
        LOG.logInfo( "-------------------------------------------------------------------------------" );
        String tmpServiceList = this.getServiceList();
        if ( tmpServiceList != null && !( "".equals( tmpServiceList.trim() ) ) ) {
            LOG.logInfo( "Initialized successfully (context '" + this.getServletContext().getServletContextName()
                         + "'):" );
            String[] tmpServices = tmpServiceList.split( "," );
            for ( String service : tmpServices ) {
                LOG.logInfo( "- " + service );
            }
        } else {
            LOG.logError( "An Error occured while initializing context '"
                          + this.getServletContext().getServletContextName() + "', no services are available." );
        }

        LOG.logInfo( "-------------------------------------------------------------------------------" );
        // Sets the attributes for tomcat -> application.getAttribute(); in jsp sites
        this.getServletContext().setAttribute( "deegree_ogc_services", this.getServiceList() );
    }

    private void initServices( ServletContext context )
                            throws ServletException {

        // get list of OGC services
        String serviceList = this.getRequiredInitParameter( SERVICE );

        String[] serviceNames = StringTools.toArray( serviceList, ",", false );

        ServiceLookup lookup = ServiceLookup.getInstance();
        for ( int i = 0; i < serviceNames.length; i++ ) {
            LOG.logInfo( StringTools.concat( 100, "---- Initializing ", serviceNames[i].toUpperCase(), " ----" ) );
            try {
                String className = this.getRequiredInitParameter( serviceNames[i] + HANDLER_CLASS );
                Class<?> handlerClzz = Class.forName( className );

                // initialize each service factory
                String s = this.getRequiredInitParameter( serviceNames[i] + HANDLER_CONF );
                URL serviceConfigurationURL = WebappResourceResolver.resolveFileLocation( s, context, LOG );

                // set configuration
                LOG.logInfo( StringTools.concat( 300, "Reading configuration for ", serviceNames[i].toUpperCase(),
                                                 " from URL: '", serviceConfigurationURL, "'." ) );

                String factoryClassName = SERVICE_FACTORIES_MAPPINGS.get( handlerClzz );

                Class<?> factory = Class.forName( factoryClassName );
                Method method = factory.getMethod( "setConfiguration", new Class[] { URL.class } );
                method.invoke( factory, new Object[] { serviceConfigurationURL } );

                // put handler to available service list
                lookup.addService( serviceNames[i].toUpperCase(), handlerClzz );

                LOG.logInfo( StringTools.concat( 300, serviceNames[i].toUpperCase(), " successfully initialized." ) );
            } catch ( ServletException e ) {
                LOG.logError( e.getMessage(), e );
            } catch ( InvocationTargetException e ) {
                e.getTargetException().printStackTrace();
                LOG.logError( this.produceMessage( ERR_MSG, new Object[] { serviceNames[i] } ), e );
            } catch ( Exception e ) {
                LOG.logError( "Can't initialize OGC service:" + serviceNames[i], e );
            }
        }
    }

    private String getRequiredInitParameter( String name )
                            throws ServletException {
        String paramValue = getInitParameter( name );
        if ( paramValue == null ) {

            String msg = "Required init parameter '" + name + "' missing in web.xml";
            LOG.logError( msg );
            throw new ServletException( msg );
        }
        return paramValue;
    }

    /**
     * @return the services, separated by ","
     */
    private String getServiceList() {

        StringBuffer buf = new StringBuffer();
        ServiceLookup lookup = ServiceLookup.getInstance();
        for ( Iterator<?> iter = lookup.getIterator(); iter.hasNext(); ) {
            String serviceName = (String) iter.next();
            buf.append( serviceName );
            if ( iter.hasNext() ) {
                buf.append( ',' );
            }
        }
        return buf.toString();
    }

    /**
     * Formats the provided string and the args array into a String using MessageFormat.
     * 
     * @param pattern
     * @param args
     * @return the message to present the client.
     */
    private String produceMessage( String pattern, Object[] args ) {
        return new MessageFormat( pattern ).format( args );
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void ctDestroyed() {
        LOG.logInfo( "Stopping context: " );

        WMSConfigurationDocument.resetCapabilitiesCache();
        WMSConfigurationDocument_1_3_0.resetCapabilitiesCache();
        WMPSConfigurationDocument.resetCapabilitiesCache();

        ServiceLookup lookup = ServiceLookup.getInstance();
        for ( Iterator<?> iter = lookup.getIterator(); iter.hasNext(); ) {
            String serviceName = (String) iter.next();
            LOG.logInfo( "Stopping service " + serviceName );

            try {
                String s = SERVICE_FACTORIES_MAPPINGS.get( lookup.getService( serviceName ) );
                Class<?> clzz = Class.forName( s );
                // TODO stop and reset all service instances
                Method[] methods = clzz.getMethods();
                for ( int j = 0; j < methods.length; j++ ) {
                    if ( methods[j].getName().equals( "reset" ) ) {
                        Object[] args = new Object[0];
                        methods[j].invoke( clzz.newInstance(), args );
                    }
                }
            } catch ( Exception e ) {
                LOG.logError( e.getMessage(), e );
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}