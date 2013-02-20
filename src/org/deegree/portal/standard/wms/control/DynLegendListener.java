//$HeadURL: svn+ssh://melmasry@svn.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/wms/control/LegendListener.java $
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
package org.deegree.portal.standard.wms.control;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMember;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCParameter;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.IDGenerator;
import org.deegree.framework.util.ImageUtils;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcbase.ImageURL;
import org.deegree.ogcwebservices.InconsistentRequestException;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilities;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilitiesDocument;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilitiesDocumentFactory;
import org.deegree.ogcwebservices.wms.operation.GetLegendGraphic;
import org.deegree.portal.Constants;
import org.deegree.portal.context.GeneralExtension;
import org.deegree.portal.context.IOSettings;
import org.deegree.portal.context.Layer;
import org.deegree.portal.context.LayerList;
import org.deegree.portal.context.ViewContext;

/**
 * will be called if the client forces a dynamic legend.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 1.23 $, $Date: 2007/02/14 15:54:38 $
 */
public class DynLegendListener extends AbstractMapListener {

    private static final ILogger LOG = LoggerFactory.getLogger( DynLegendListener.class );

    private int leftMargin = 15;

    private int rightMargin = 15;

    private int topMargin = 20;

    private int bottomMargin = 20;

    private boolean useLayerTitle = true;

    private BufferedImage separator;

    private BufferedImage missingImg;

    private static Map<String, Object> wmscache = new HashMap<String, Object>();

    private static Map<String, BufferedImage> legendSymCache = new HashMap<String, BufferedImage>();

    private List<String> missing = null;

    private int maxNNLegendSize = 50;

    private Properties userNames = new Properties();

    private Properties passwords = new Properties();

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.portal.standard.wms.control.AbstractMapListener#actionPerformed(org.deegree.enterprise.control.FormEvent)
     */
    @Override
    public void actionPerformed( FormEvent event ) {

        super.actionPerformed( event );
        missing = new ArrayList<String>();

        try {
            init();
        } catch ( IOException e ) {
            LOG.logError( "DynLegendListener: Error occurred initializing DynLegendListener: ", e );
            gotoErrorPage( "Error occurred initializing DynLegendListener: " + e );
        }

        RPCWebEvent rpc = (RPCWebEvent) event;
        RPCMethodCall mc = rpc.getRPCMethodCall();
        RPCParameter[] para = mc.getParameters();
        RPCStruct struct = (RPCStruct) para[0].getValue();
        HttpSession session = ( (HttpServletRequest) this.getRequest() ).getSession();
        ViewContext vc = (ViewContext) session.getAttribute( Constants.CURRENTMAPCONTEXT );
        HashMap[] model = createWMSRequestModel( struct );

        try {
            GetLegendGraphic legendParam = getLegendRequestParameter();
            HashMap<String, Object> symbols = (HashMap<String, Object>) readLegendSymbols( legendParam, model, vc );
            Rectangle rect = calcLegendSize( symbols );
            BufferedImage bi = new BufferedImage( rect.width + leftMargin + rightMargin, rect.height + topMargin
                                                                                         + bottomMargin,
                                                  BufferedImage.TYPE_INT_RGB );
            Color color = Color.decode( (String) struct.getMember( "bgColor" ).getValue() );
            bi = drawSymbolsToBI( symbols, bi, color );
            saveImage( vc, bi );

            getRequest().setAttribute( "LEGENDWIDTH", String.valueOf( bi.getWidth() ) );
            getRequest().setAttribute( "LEGENDHEIGHT", String.valueOf( bi.getHeight() ) );
        } catch ( Exception e ) {
            LOG.logError( "DynLegendListener: Error occurred in DynLegendListener: ", e );
            gotoErrorPage( "Error occurred in DynLegendListener: " + e );
        }

    }

    /**
     * Reads the initParams from the controller.xml and assigns them to the global variables
     * 
     * @throws IOException
     */
    private void init()
                            throws IOException {
        String path = null;

        String tmp = getInitParameter( "leftMargin" );
        if ( tmp != null ) {
            leftMargin = Integer.parseInt( tmp );
        }
        tmp = getInitParameter( "rightMargin" );
        if ( tmp != null ) {
            rightMargin = Integer.parseInt( tmp );
        }
        tmp = getInitParameter( "topMargin" );
        if ( tmp != null ) {
            topMargin = Integer.parseInt( tmp );
        }
        tmp = getInitParameter( "bottomMargin" );
        if ( tmp != null ) {
            bottomMargin = Integer.parseInt( tmp );
        }
        tmp = getInitParameter( "useLayerTitle" );
        useLayerTitle = "true".equalsIgnoreCase( tmp );

        tmp = getInitParameter( "separator" );
        if ( tmp != null ) {
            path = StringTools.concat( 200, getHomePath(), tmp );
            LOG.logDebug( "PATH = ", path );
            separator = ImageUtils.loadImage( path );
        }

        tmp = getInitParameter( "missingImage" );
        if ( tmp != null ) {
            path = StringTools.concat( 200, getHomePath(), tmp );
            LOG.logDebug( "PATH = ", path );
            missingImg = ImageUtils.loadImage( path );
        } else {
            missingImg = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB );
        }
        tmp = getInitParameter( "maxNNLegendSize" );
        if ( tmp != null ) {
            maxNNLegendSize = Integer.parseInt( tmp );
        }
        tmp = getInitParameter( "users" );
        if ( tmp != null ) {
            String[] t = StringTools.toArray( tmp, ";|", false );
            for ( int i = 0; i < t.length; i += 3 ) {
                userNames.put( t[i], t[i + 1] );
                passwords.put( t[i], t[i + 2] );
            }
        }
    }

    /**
     * takes in a HashMap holding the properties of the legend and returns the size of the legend to
     * be
     * 
     * @param map
     *            Hashmap holding the GetLegendGraphic properties
     * @return A rectangle holding the legend size
     */
    private Rectangle calcLegendSize( HashMap map ) {

        String[] layers = (String[]) map.get( "NAMES" );
        String[] titles = (String[]) map.get( "TITLES" );
        BufferedImage[] legs = (BufferedImage[]) map.get( "IMAGES" );

        int w = 0;
        int h = 0;
        for ( int i = 0; i < layers.length; i++ ) {
            h += legs[i].getHeight() + 5;
            if ( separator != null && i < layers.length - 1 ) {
                h += separator.getHeight() + 5;
            }
            Graphics g = legs[i].getGraphics();
            if ( useLayerTitle && legs[i].getHeight() < maxNNLegendSize && !missing.contains( layers[i] ) ) {
                Rectangle2D rect = g.getFontMetrics().getStringBounds( titles[i], g );
                g.dispose();
                if ( ( rect.getWidth() + legs[i].getWidth() ) > w ) {
                    w = (int) rect.getWidth() + legs[i].getWidth();
                }
            } else {
                if ( legs[i].getWidth() > w ) {
                    w = legs[i].getWidth();
                }
            }
        }
        // w += 20;

        return new Rectangle( w, h );
    }

    /**
     * Draws the given symbol to the given image
     * 
     * @param map
     *            Hashmap holding the properties of the legend
     * @param bi
     *            image of the legend
     * @param color
     *            color to fill the graphic
     * @return The drawn BufferedImage
     */
    private BufferedImage drawSymbolsToBI( HashMap map, BufferedImage bi, Color color ) {

        Graphics g = bi.getGraphics();
        g.setColor( color );
        g.fillRect( 0, 0, bi.getWidth(), bi.getHeight() );

        String[] layers = (String[]) map.get( "NAMES" );
        String[] titles = (String[]) map.get( "TITLES" );
        BufferedImage[] legs = (BufferedImage[]) map.get( "IMAGES" );
        int h = topMargin;
        for ( int i = layers.length - 1; i >= 0; i-- ) {
            g.drawImage( legs[i], leftMargin, h, null );
            g.setColor( Color.BLACK );
            // just draw title if the flag has been set in listener configuration,
            // the legend image is less than a defined value and a legend image
            // (not missing) has been accessed
            if ( useLayerTitle && legs[i].getHeight() < maxNNLegendSize && !missing.contains( layers[i] ) ) {
                g.drawString( titles[i], leftMargin + legs[i].getWidth() + 10, h + (int) ( legs[i].getHeight() / 1.2 ) );
            }
            h += legs[i].getHeight() + 5;
            if ( separator != null && i > 0 ) {
                g.drawImage( separator, leftMargin, h, null );
                h += separator.getHeight() + 5;
            }
        }
        g.dispose();
        return bi;
    }

    /**
     * Receives an RPCStruct(WMSRequest) and extracts all its properties to a HashMap
     * 
     * @param struct
     * @return a HashMap contains the WMSRequest properties
     */
    private HashMap[] createWMSRequestModel( RPCStruct struct ) {

        RPCMember[] member = struct.getMembers();
        String request = "";
        List<Map<String, String>> list = new ArrayList<Map<String, String>>( member.length - 1 );
        for ( int i = 0; i < member.length; i++ ) {
            if ( !member[i].getName().equals( "bgColor" ) && !member[i].getName().equals( "sessionID" ) ) {
                request = (String) member[i].getValue();
                Map<String, String> map = toMap( request );
                StringTokenizer st = new StringTokenizer( request, "?" );
                map.put( "URL", st.nextToken() );
                list.add( map );
            }
        }
        HashMap[] getMR = new HashMap[list.size()];
        return list.toArray( getMR );
    }

    /**
     * Saves the given image to the print/images directory of the given ViewContext
     * 
     * @param vc
     * @param bg
     */
    private void saveImage( ViewContext vc, BufferedImage bg ) {

        GeneralExtension ge = vc.getGeneral().getExtension();
        IOSettings ios = ge.getIOSettings();
        String dir = ios.getPrintDirectory().getDirectoryName();
        String format = "png";
        long l = IDGenerator.getInstance().generateUniqueID();
        String file = StringTools.concat( 50, "legend", l, ".", format );
        int pos = dir.lastIndexOf( '/' );
        String access = StringTools.concat( 100, "./", dir.substring( pos + 1, dir.length() ), "/", file );

        String temp = StringTools.concat( 100, "legend", ( l - 50 ), ".", format );
        File f = new File( StringTools.concat( 100, "./", dir.substring( pos + 1, dir.length() ), "/", temp ) );
        try {
            if ( f.exists() ) {
                f.delete();
            }
        } catch ( Exception e ) {
            if ( LOG.getLevel() == ILogger.LOG_ERROR ) {
                LOG.logError( StringTools.concat( 100, "DynLegendListener: The temporary image file: ", f.getPath(),
                                                  " could not be deleted" ) );
            }
        }
        try {
            String path = new String( StringTools.concat( 100, dir, "/", file ) );
            if ( path.startsWith( "file:/" ) ) {
                path = new URL( path ).getFile();
            }
            FileOutputStream fos = new FileOutputStream( new File( path ) );
            ImageUtils.saveImage( bg, fos, format, 1 );
            fos.close();

        } catch ( Exception e ) {
            LOG.logError( "DynLegendListener: Error occurred in saving legend image: ", e );
        }
        this.getRequest().setAttribute( "LEGENDURL", access );

    }

    /**
     * Creates a GetLegendGraphic Request
     * 
     * @return An instance of GetLegendGraphic
     * @throws XMLParsingException
     * @throws MalformedURLException
     * @throws InconsistentRequestException
     */
    private GetLegendGraphic getLegendRequestParameter()
                            throws InconsistentRequestException {

        HashMap<String, String> legend = toMap( "VERSION=1.1.1&REQUEST=GetLegendGraphic&FORMAT=image/png&WIDTH=50&HEIGHT=50&"
                                                + "EXCEPTIONS=application/vnd.ogc.se_inimage&LAYER=europe:major_rivers&STYLE=default" );

        GetLegendGraphic legendReq = GetLegendGraphic.create( legend );

        return legendReq;
    }

    /**
     * Creates legend symbols and stores them in a hashmap through the given params
     * 
     * @param glr
     * @param model
     *            A HashMap holding the Request params
     * @param vc
     *            Current layer list
     * @return A HashMap holding the legend symbols as read from the cash
     * @throws IOException
     */
    private Map<String, Object> readLegendSymbols( GetLegendGraphic glr, HashMap[] model, ViewContext vc )
                            throws IOException {

        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<BufferedImage> list2 = new ArrayList<BufferedImage>();
        ArrayList<String> list3 = new ArrayList<String>();

        LayerList ll = vc.getLayerList();

        StringTokenizer st = null;
        String format = glr.getFormat();
        if ( format.equals( "image/jpg" ) ) {
            format = "image/jpeg";
        }

        int lgHeight = 0;
        for ( int i = 0; i < model.length; i++ ) {
            if ( model[i].get( "LAYERS" ) != null ) {
                String style = (String) model[i].get( "STYLES" );
                String[] styles = new String[100];
                if ( style != null ) {
                    styles = StringTools.toArray( style, ",", false );
                }

                // read capabilities for current service (URL) or - if avalable -
                // read it from the cache
                String addr = (String) model[i].get( "URL" );
                if ( wmscache.get( addr ) == null ) {
                    if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
                        LOG.logDebug( StringTools.concat( 200, "DynLegendListener: Adding the caps of ", addr,
                                                          " to the hash map" ) );
                    }
                    wmscache.put( addr, getCapabilities( addr, (String) model[i].get( "VERSION" ) ) );
                } else {
                    LOG.logDebug( "DynLegendListener: read capabilities from cache" );

                }
                LOG.logDebug( "DynLegendListener: caps ", addr );
                WMSCapabilities capa = (WMSCapabilities) wmscache.get( addr );

                st = new StringTokenizer( (String) model[i].get( "LAYERS" ), "," );
                int k = 0;
                while ( st.hasMoreTokens() ) {
                    if ( styles == null || styles.length == 0 || styles[k] == null || styles[k].equals( "" ) ) {
                        style = "default";
                    } else {
                        style = styles[k];
                    }
                    k++;

                    String layer = st.nextToken();
                    String title = layer;
                    if ( capa.getLayer( layer ) != null ) {
                        title = capa.getLayer( layer ).getTitle();
                    }

                    BufferedImage legendGraphic = null;
                    String path = StringTools.concat( 200, addr, "%%", layer, "%%", style );
                    if ( legendSymCache.get( path ) != null ) {
                        legendGraphic = legendSymCache.get( path );
                        LOG.logDebug( "DynLegendListener: read legendsymbol from cache" );
                    } else {

                        // access layer from WMC. If current WMC does not offer
                        // a layer named like the passed the layer may has been
                        // added dynamically
                        LOG.logDebug( "legendURL for layer; ", layer + " from " + addr );
                        Layer lay = ll.getLayer( layer, addr );
                        if ( lay == null ) {
                            LOG.logWarning( "layer: " + layer + " not found" );
                        }
                        ImageURL imgUrl = null;
                        URL url = null;
                        if ( lay != null && lay.getStyleList() != null && lay.getStyleList().getStyle( style ) != null
                             && ( imgUrl = lay.getStyleList().getStyle( style ).getLegendURL() ) != null ) {
                            LOG.logDebug( "DynLegendListener: An image url could be fetched" );
                            url = imgUrl.getOnlineResource();
                        } else {
                            LOG.logDebug( "DynLegendListener: No url found. Using given image in controller.xml" );
                        }

                        lgHeight = lgHeight + 30;

                        if ( url != null ) {
                            LOG.logDebug( "DynLegendListener: URL is valid. Loading Legend Graphic..." );
                            LOG.logDebug( "ImgUrl is: ", url.toExternalForm() );
                            // TODO catch error from loadImage, but also log the error message.
                            // error might occure, if configuration is wrong.
                            // (trying to read from a not existing feature)
                            legendGraphic = ImageUtils.loadImage( url );
                        } else {
                            LOG.logDebug( "DynLegendListener: URL is null. Drawing the image from a missingImage variable in initparams" );

                            // created and draw an image with layers name if no legend symbol
                            // can be accessed
                            legendGraphic = new BufferedImage( 2, 30, BufferedImage.TYPE_INT_RGB );
                            Graphics g = legendGraphic.getGraphics();
                            // calculate size of layer name when drawed on the image
                            Rectangle2D rect = g.getFontMetrics().getStringBounds( title, g );
                            g.dispose();

                            legendGraphic = new BufferedImage( rect.getBounds().width + 80, missingImg.getHeight() + 3,
                                                               BufferedImage.TYPE_INT_ARGB );
                            g = legendGraphic.getGraphics();
                            g.drawImage( missingImg, 0, 0, null );
                            g.setColor( Color.RED );
                            g.drawString( title, missingImg.getWidth() + 5, missingImg.getHeight() / 2 );
                            g.dispose();
                            missing.add( layer );
                        }
                        legendSymCache.put( path, legendGraphic );
                    }
                    list1.add( layer );
                    list2.add( legendGraphic );
                    list3.add( title );
                }
            }
        }

        String[] layers = list1.toArray( new String[list1.size()] );
        BufferedImage[] legs = list2.toArray( new BufferedImage[list2.size()] );
        String[] titles = list3.toArray( new String[list3.size()] );
        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "NAMES", layers );
        map.put( "TITLES", titles );
        map.put( "IMAGES", legs );

        return map;
    }

    /**
     * Performs a GetCapabilities request
     * 
     * @param url
     * @param version
     * @return the obtained WMSCapbilities
     */
    private WMSCapabilities getCapabilities( String url, String version ) {

        WMSCapabilities capa = null;
        HttpClient httpclient = new HttpClient();
        httpclient.getHttpConnectionManager().getParams().setSoTimeout( 15000 );
        StringBuffer sb = new StringBuffer( 500 );
        sb.append( url );
        sb.append( "?request=GetCapabilities&service=WMS&version=" );
        sb.append( version );

        if ( userNames.get( url ) != null ) {
            sb.append( "&USER=" ).append( userNames.get( url ) );
            sb.append( "&PASSWORD=" );
            if ( passwords.get( url ) != null ) {
                sb.append( passwords.get( url ) );
            }
        }
        LOG.logDebug( "DynLegendListener: ", sb );
        GetMethod httpget = new GetMethod( sb.toString() );
        try {
            httpclient.executeMethod( httpget );
            XMLFragment xml = new XMLFragment();
            xml.load( new InputStreamReader( httpget.getResponseBodyAsStream() ), XMLFragment.DEFAULT_URL );
            WMSCapabilitiesDocument cdoc = WMSCapabilitiesDocumentFactory.getWMSCapabilitiesDocument( xml.getRootElement() );
            capa = (WMSCapabilities) cdoc.parseCapabilities();
        } catch ( Exception e ) {
            LOG.logError( "DynLegendListener: GetCaps failed for " + sb.toString() );
        }
        return capa;
    }
}