//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsproxy/OWSProxyServletFilter.java $
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
package org.deegree.security.owsproxy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.enterprise.servlet.ServletRequestWrapper;
import org.deegree.enterprise.servlet.ServletResponseWrapper;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.ImageUtils;
import org.deegree.framework.util.MimeTypeMapper;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.ogcbase.BaseURL;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCRequestFactory;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.wcs.getcoverage.GetCoverage;
import org.deegree.ogcwebservices.wms.operation.GetMap;
import org.deegree.security.SecurityConfigurationException;
import org.deegree.security.UnauthorizedException;
import org.deegree.security.drm.SecurityAccessManager;
import org.deegree.security.drm.model.User;
import org.deegree.security.owsrequestvalidator.OWSValidator;
import org.deegree.security.owsrequestvalidator.Policy;
import org.deegree.security.owsrequestvalidator.PolicyDocument;
import org.w3c.dom.Document;

/**
 * An OWSProxyPolicyFilter can be registered as a ServletFilter to a web context. It offeres a
 * facade that looks like a OWS but additionaly enables validating incoming requests and outgoing
 * responses against rules defined in a policy document and/or a deegree user and right management
 * system.
 * 
 * @see org.deegree.security.drm.SecurityRegistry
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 10662 $, $Date: 2008-03-25 09:32:36 +0100 (Di, 25. Mär 2008) $
 * @deprecated use
 * @see ConfigurableOWSProxyServletFilter
 */
@Deprecated
public class OWSProxyServletFilter implements Filter {

    private static final ILogger LOG = LoggerFactory.getLogger( OWSProxyServletFilter.class );

    private static final NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    private FilterConfig config;

    private OWSProxyPolicyFilter pFilter;

    // private Policy policy = null;
    private SecurityConfig secConfig;

    private String altRequestPage;

    private String altResponsePage;

    private boolean imageExpected = false;

    /**
     * initialize the filter with parameters from the deployment descriptor
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @SuppressWarnings("unchecked")
    public void init( FilterConfig config )
                            throws ServletException {
        this.config = config;

        Properties validators = new Properties();
        try {
            InputStream is = OWSProxyServletFilter.class.getResourceAsStream( "validators.properties" );
            validators.load( is );
            is.close();
        } catch ( Exception e ) {
            throw new ServletException( e );
        }

        pFilter = new OWSProxyPolicyFilter();
        String proxyURL = "http://127.0.0.1/owsproxy/proxy";
        if ( config.getInitParameter( "PROXYURL" ) != null ) {
            proxyURL = config.getInitParameter( "PROXYURL" );
        }
        Enumeration<String> iterator = config.getInitParameterNames();
        while ( iterator.hasMoreElements() ) {
            String paramName = iterator.nextElement();
            String paramValue = config.getInitParameter( paramName );
            if ( paramName.endsWith( "POLICY" ) ) {
                paramValue = config.getServletContext().getRealPath( paramValue );
                File file = new File( paramValue );
                URL fileURL = null;
                try {
                    fileURL = file.toURI().toURL();
                } catch ( MalformedURLException e ) {
                    LOG.logError( "Couldn't create an url from the configured POLICY parameter: " + paramValue
                                  + " because: " + e.getMessage() );
                    throw new ServletException( e );
                }
                if ( fileURL != null ) {
                    LOG.logDebug( "OWSProxyFilter: reading configuration file from : " + fileURL.toExternalForm() );
                    initValidator( proxyURL, paramName, fileURL, validators );
                }
            }

        }
        // } catch ( Exception e ) {
        // LOG.logError( e.getMessage(), e );
        // throw new ServletException( e );
        // }
        LOG.logInfo( "OWSProxyServlet intitialized successfully" );
        altRequestPage = config.getInitParameter( "ALTREQUESTPAGE" );
        altResponsePage = config.getInitParameter( "ALTRESPONSEPAGE" );
    }

    /**
     * 
     * @param proxyURL
     * @param paramName
     * @param paramValue
     * @param validators
     * @throws ServletException
     */
    private void initValidator( String proxyURL, String paramName, URL paramValue, Properties validators )
                            throws ServletException {
        try {
            PolicyDocument doc = new PolicyDocument( paramValue );
            Policy policy = doc.getPolicy();
            if ( secConfig == null && policy.getSecurityConfig() != null ) {
                // use security configuration of the first policy that defined one.
                // this is possible because just one security configuration can be
                // used within a deegree/VM instance
                secConfig = policy.getSecurityConfig();
            }
            int pos = paramName.indexOf( ':' );
            String service = paramName.substring( 0, pos );

            // describes the signature of the required constructor
            Class<?>[] cl = new Class<?>[2];
            cl[0] = Policy.class;
            cl[1] = String.class;

            // set parameter to submitt to the constructor
            Object[] o = new Object[2];
            o[0] = policy;
            o[1] = proxyURL;

            Class<?> clzz = Class.forName( validators.getProperty( service ) );
            Constructor<?> con = clzz.getConstructor( cl );

            pFilter.addValidator( service, (OWSValidator) con.newInstance( o ) );
        } catch ( SecurityConfigurationException e ) {
            LOG.logError( "Couldn't create a policy document from given value: " + paramValue + ", because : "
                          + e.getMessage() );
            throw new ServletException( e );
        } catch ( XMLParsingException e ) {
            LOG.logError( "Couldn't create a policy from given value: " + paramValue + ", because : " + e.getMessage() );
            throw new ServletException( e );
        } catch ( ClassNotFoundException e ) {
            LOG.logError( "The classloader couldn't find an appropriate class  for the configured service, because"
                          + e.getMessage() );
            throw new ServletException( e );
        } catch ( NoSuchMethodException e ) {
            LOG.logError( "The classloader couldn't find a constructor for the configured service, because"
                          + e.getMessage() );
            throw new ServletException( e );
        } catch ( InstantiationException e ) {
            LOG.logError( "The classloader couldn't instantiate the configured service, because" + e.getMessage() );
            throw new ServletException( e );
        } catch ( IllegalAccessException e ) {
            LOG.logError( "The classloader couldn't instantiate the configured service, because" + e.getMessage() );
            throw new ServletException( e );
        } catch ( InvocationTargetException e ) {
            LOG.logError( "The classloader couldn't instantiate the configured service, because" + e.getMessage() );
            throw new ServletException( e );
        }
    }

    /**
     * free resources allocated by the filter
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        config = null;
    }

    /**
     * perform filter
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                            throws IOException, ServletException {

        // Map<String, String[]> params = request.getParameterMap();

        // encapsulate the servelt request into a wrapper object to ensure
        // the availability of the InputStream
        ServletRequestWrapper requestWrapper = null;

        if ( request instanceof ServletRequestWrapper ) {
            LOG.logDebug( "OWSProxySerlvetFilter: the incoming request is actually an org.deegree.enterprise.servlet.RequestWrapper, so not creating new instance." );
            requestWrapper = (ServletRequestWrapper) request;
        } else {
            requestWrapper = new ServletRequestWrapper( (HttpServletRequest) request );
        }

        LOG.logDebug( "OWSProxySerlvetFilter: GetContentype(): " + requestWrapper.getContentType() );

        OGCWebServiceRequest owsReq = null;
        try {
            owsReq = OGCRequestFactory.create( requestWrapper );
        } catch ( OGCWebServiceException e ) {
            LOG.logError( "OWSProxyServletFilter: Couln't create an OGCWebserviceRequest because: " + e.getMessage(), e );
            throw new ServletException( e.getMessage() );
        }
        imageExpected = isImageRequested( owsReq );
        // extract user from the request
        User user = null;
        try {
            user = getUser( requestWrapper, owsReq );
        } catch ( Exception e1 ) {
            handleResponseMissingAutorization( (HttpServletRequest) request, (HttpServletResponse) response, owsReq,
                                               e1.getMessage() );
            return;
        }
        try {
            pFilter.validateGeneralConditions( (HttpServletRequest) request, requestWrapper.getContentLength(), user );
            pFilter.validate( owsReq, user );
        } catch ( InvalidParameterValueException e ) {
            handleRequestMissingAutorization( (HttpServletRequest) request, (HttpServletResponse) response, owsReq,
                                              e.getMessage() );
            return;
        } catch ( UnauthorizedException e ) {
            handleRequestMissingAutorization( (HttpServletRequest) request, (HttpServletResponse) response, owsReq,
                                              e.getMessage() );
            return;
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            request.setAttribute( "MESSAGE", e.getMessage() );
            ServletContext sc = config.getServletContext();
            sc.getRequestDispatcher( altResponsePage ).forward( request, response );
            return;
        }
        // encapsulate the servelt response into a wrapper object to ensure
        // the availability of the OutputStream
        ServletResponseWrapper resWrap = new ServletResponseWrapper( (HttpServletResponse) response );
        logHttpRequest( requestWrapper );
        // forward request to the next filter or servlet
        chain.doFilter( requestWrapper, resWrap );
        // get result from performing the request
        OutputStream os = resWrap.getOutputStream();
        byte[] b = ( (ServletResponseWrapper.ProxyServletOutputStream) os ).toByteArray();

        if ( !imageExpected ) {
            LOG.logDebug( new String( b ) );
        }
        try {
            // validate the result of a request performing
            String mime = resWrap.getContentType();
            LOG.logDebug( "mime type raw: " + mime );
            if ( mime != null ) {
                mime = StringTools.toArray( mime, ";", false )[0];
            } else {
                if ( imageExpected ) {
                    mime = "image/jpeg";
                } else {
                    mime = "text/xml";
                }
            }
            LOG.logDebug( "mime type: " + mime );
            b = pFilter.validate( owsReq, b, mime, user );
        } catch ( InvalidParameterValueException ee ) {
            LOG.logError( ee.getMessage(), ee );
            handleResponseMissingAutorization( (HttpServletRequest) request, (HttpServletResponse) response, owsReq,
                                               ee.getMessage() );
            return;
        } catch ( UnauthorizedException e ) {
            LOG.logError( e.getMessage(), e );
            handleResponseMissingAutorization( (HttpServletRequest) request, (HttpServletResponse) response, owsReq,
                                               e.getMessage() );
            return;
        }

        response.setContentType( resWrap.getContentType() );
        // write result back to the client
        os = response.getOutputStream();
        os.write( b );
        os.close();
    }

    /**
     * logs a requests parameters and meta informations
     * 
     * @param reqWrap
     */
    private void logHttpRequest( ServletRequestWrapper reqWrap ) {
        if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
            LOG.logDebug( "getRemoteAddr " + reqWrap.getRemoteAddr() );
            LOG.logDebug( "getRemotePort " + reqWrap.getRemotePort() );
            LOG.logDebug( "getLocalPort " + reqWrap.getLocalPort() );
            LOG.logDebug( "getMethod " + reqWrap.getMethod() );
            LOG.logDebug( "getQueryString " + reqWrap.getQueryString() );
            LOG.logDebug( "getPathInfo " + reqWrap.getPathInfo() );
            LOG.logDebug( "getRequestURI " + reqWrap.getRequestURI() );
            LOG.logDebug( "getServerName " + reqWrap.getServerName() );
            LOG.logDebug( "getServerPort " + reqWrap.getServerPort() );
            LOG.logDebug( "getServletPath " + reqWrap.getServletPath() );
        }
    }

    /**
     * go to alternative page if autorization to perform the desired request ist missing
     * 
     * @param request
     * @param response
     * @param owsReq
     * @param message
     * @throws IOException
     * @throws ServletException
     */
    private void handleRequestMissingAutorization( HttpServletRequest request, HttpServletResponse response,
                                                   OGCWebServiceRequest owsReq, String message )
                            throws IOException, ServletException {
        if ( message == null ) {
            message = "missing authorization";
        }
        if ( imageExpected ) {
            int width = 500;
            int height = 500;
            if ( owsReq != null && owsReq instanceof GetMap ) {
                width = ( (GetMap) owsReq ).getWidth();
                height = ( (GetMap) owsReq ).getHeight();
            } else if ( owsReq != null && owsReq instanceof GetCoverage ) {
                Envelope env = (Envelope) ( (GetCoverage) owsReq ).getDomainSubset().getSpatialSubset().getGrid();
                width = (int) env.getWidth();
                height = (int) env.getHeight();
            }
            response.setContentType( "image/jpeg" );
            OutputStream os = response.getOutputStream();
            BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            Graphics g = bi.getGraphics();
            g.setColor( Color.WHITE );
            g.fillRect( 0, 0, width, height );
            g.setColor( Color.BLACK );
            g.setFont( new Font( "DIALOG", Font.PLAIN, 14 ) );
            g.drawString( Messages.getString( "MISSINGAUTHORIZATION" ), 5, 60 );
            String[] lines = StringTools.toArray( message, ":|", false );
            int y = 100;
            for ( int i = 0; i < lines.length; i++ ) {
                g.drawString( lines[i], 5, y );
                y = y + 30;
            }
            g.dispose();
            try {
                ImageUtils.saveImage( bi, os, "jpeg", 0.95f );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            os.close();
        } else {
            request.setAttribute( "MESSAGE", message );
            ServletContext sc = config.getServletContext();
            sc.getRequestDispatcher( altRequestPage ).forward( request, response );
        }
    }

    /**
     * go to alternative page if autorization to deliver the result to a request is missing
     * 
     * @param request
     * @param response
     * @param owsReq
     * @param message
     * @throws IOException
     * @throws ServletException
     */
    private void handleResponseMissingAutorization( HttpServletRequest request, HttpServletResponse response,
                                                    OGCWebServiceRequest owsReq, String message )
                            throws IOException, ServletException {

        if ( imageExpected ) {
            int width = 500;
            int height = 500;
            if ( owsReq != null && owsReq instanceof GetMap ) {
                width = ( (GetMap) owsReq ).getWidth();
                height = ( (GetMap) owsReq ).getHeight();
            } else if ( owsReq != null && owsReq instanceof GetCoverage ) {
                Envelope env = (Envelope) ( (GetCoverage) owsReq ).getDomainSubset().getSpatialSubset().getGrid();
                width = (int) env.getWidth();
                height = (int) env.getHeight();
            }
            response.setContentType( "image/jpeg" );
            OutputStream os = response.getOutputStream();
            BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
            Graphics g = bi.getGraphics();
            g.setColor( Color.WHITE );
            g.fillRect( 0, 0, width, height );
            g.setColor( Color.BLACK );
            g.setFont( new Font( "DIALOG", Font.PLAIN, 14 ) );
            String[] lines = StringTools.toArray( message, ":|", false );
            int y = 100;
            for ( int i = 0; i < lines.length; i++ ) {
                g.drawString( lines[i], 5, y );
                y = y + 30;
            }
            g.dispose();
            try {
                ImageUtils.saveImage( bi, os, "jpeg", 0.95f );
            } catch ( Exception e ) {
                LOG.logError( e.getMessage(), e );
            }
            os.write( message.getBytes() );
            os.close();
        } else {
            request.setAttribute( "MESSAGE", message );
            ServletContext sc = config.getServletContext();
            sc.getRequestDispatcher( altResponsePage ).forward( request, response );
        }
    }

    /**
     * returns the user from the incomming request. The extraction of the user takes three steps
     * <ul>
     * <li>1. get the vendorspecific parameter 'USER' & 'PASSWORD'
     * <li>2. if 1.) is null get the remote users name (request.getRemoteUser())
     * </ul>
     * 
     * @param request
     * @return the user from the incomming request.
     * @throws InvalidParameterValueException
     */
    private User getUser( HttpServletRequest request, OGCWebServiceRequest owsReq )
                            throws UnauthorizedException, IOException, InvalidParameterValueException {

        String sessionId = owsReq.getVendorSpecificParameter( "SESSIONID" );
        String user = owsReq.getVendorSpecificParameter( "USER" );
        String password = null;
        if ( user != null ) {
            LOG.logDebug( "get user from user/password parameter" );
            return authentificateFromUserPw( owsReq );
        } else if ( sessionId == null && request.getUserPrincipal() != null ) {
            LOG.logDebug( "get user from UserPrinicipal" );
            user = request.getUserPrincipal().getName();
            if ( user.indexOf( "\\" ) > 1 ) {
                String[] us = StringTools.toArray( user, "\\", false );
                user = us[us.length - 1];
            }
        } else if ( secConfig != null && sessionId != null ) {
            LOG.logDebug( "get user from WAS/sessionID" );
            AuthentificationSettings as = secConfig.getAuthsettings();
            if ( as != null ) {
                BaseURL baseUrl = as.getAuthentificationURL();
                String tmp[] = getUserFromWAS( baseUrl.getOnlineResource().toExternalForm(), sessionId );
                user = tmp[0];
                password = tmp[1];
            }
        } else {
            LOG.logDebug( "get user as source IP address because wether USER, "
                          + "SESSIONID nor Userprincipal are available" );
            user = request.getRemoteAddr();
        }
        LOG.logDebug( StringTools.concat( 100, "USER: ", user, "/", password ) );
        User usr = null;
        try {
            if ( user != null && SecurityAccessManager.isInitialized() ) {
                SecurityAccessManager sam = SecurityAccessManager.getInstance();

                usr = sam.getUserByName( user );
                if ( request.getUserPrincipal() == null ) {
                    // a user just must authenticate himself if he is
                    // not identified by its user name being send within
                    // the HTTP header
                    usr.authenticate( password );
                } else {
                    // if user is read from UserPrincipal his password must
                    // be read from security management
                    usr.authenticate( sam.getUserByName( user ).getPassword() );
                }
            }
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new UnauthorizedException( Messages.format( "OWSProxyServletFilter.USERERROR", user ) );
        }

        return usr;
    }

    /**
     * Authenticates a user if he is identified by its name and password passed as vendorspecific
     * parameters with an OGC service request
     * 
     * @param owsReq
     * @return the user
     * @throws UnauthorizedException
     * @throws InvalidParameterValueException
     */
    private User authentificateFromUserPw( OGCWebServiceRequest owsReq )
                            throws UnauthorizedException, InvalidParameterValueException {
        String user = owsReq.getVendorSpecificParameter( "USER" );
        String password = owsReq.getVendorSpecificParameter( "PASSWORD" );

        LOG.logDebug( "USER: ", user );
        LOG.logDebug( "PASSWORD: ", password );
        if ( password == null ) {
            throw new InvalidParameterValueException( Messages.getString( "PASSWORDMISSING" ) );
        }

        User usr = null;
        try {
            SecurityAccessManager sam = SecurityAccessManager.getInstance();
            usr = sam.getUserByName( user );
            usr.authenticate( password );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            if ( !( user.equals( "anonymous" ) ) ) {
                throw new UnauthorizedException( Messages.format( "OWSProxyServletFilter.USERERROR", user ) );
            }
        }

        return usr;
    }

    /**
     * access user informations from a remote WAAS. an array of Strings will be returned. with
     * <ul>
     * <li>[0] = user name
     * <li>[1] = the users password
     * </ul>
     * 
     * @param sessionID
     * @return all users.
     * @throws IOException
     */
    private String[] getUserFromWAS( String urlStr, String sessionID )
                            throws IOException {
        String[] user = new String[3];
        try {
            StringBuffer sb = new StringBuffer( 200 );
            sb.append( urlStr ).append( "?REQUEST=DescribeUser&Service=WAS&" );
            sb.append( "SESSIONID=" ).append( sessionID ).append( "&version=1.0.0" );
            URL url = new URL( sb.toString() );
            InputStreamReader isr = new InputStreamReader( url.openStream() );
            Document doc = XMLTools.parse( isr );
            user[0] = XMLTools.getNodeAsString( doc, "/User/UserName", nsContext, null );
            user[1] = XMLTools.getNodeAsString( doc, "/User/Password", nsContext, null );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new IOException( Messages.getString( "OWSProxyServletFilter.WASACCESS" ) );
        }
        return user;
    }

    private boolean isImageRequested( OGCWebServiceRequest request ) {
        boolean imageReq = false;

        if ( request instanceof GetMap ) {
            imageReq = ( (GetMap) request ).getExceptions().indexOf( "image" ) > -1;
        } else if ( request instanceof GetCoverage ) {
            String format = ( (GetCoverage) request ).getOutput().getFormat().getCode();
            imageReq = MimeTypeMapper.isKnownImageType( "image/" + format );
        }

        LOG.logDebug( "authorization problems expected to be returned as image: ", imageReq );

        return imageReq;
    }

}
