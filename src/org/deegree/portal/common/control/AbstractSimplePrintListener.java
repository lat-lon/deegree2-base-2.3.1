//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/common/control/AbstractSimplePrintListener.java $
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
package org.deegree.portal.common.control;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;

import org.deegree.enterprise.control.AbstractListener;
import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMember;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.ImageUtils;
import org.deegree.framework.util.KVP2Map;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.model.spatialschema.Point;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.InconsistentRequestException;
import org.deegree.ogcwebservices.wms.operation.GetMap;
import org.deegree.portal.PortalException;
import org.deegree.portal.PortalUtils;
import org.deegree.portal.context.Layer;
import org.deegree.portal.context.Style;
import org.deegree.portal.context.ViewContext;
import org.deegree.security.drm.model.User;
import org.xml.sax.SAXException;

/**
 * performs a print request/event by creating a PDF document from the current map. For this JASPER is used. Well known
 * parameters that can be passed to a jaser report are:<br>
 * <ul>
 * <li>MAP</li>
 * <li>LEGEND</li>
 * <li>DATE</li>
 * <li>MAPSCALE</li>
 * </ul>
 * <br>
 * Additionaly parameters named 'TA:XXXX' can be used. deegree will create a k-v-p for each TA:XXXX passed as part of
 * RPC.
 * 
 * 
 * @version $Revision: 12678 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: melmasry $
 * 
 * @version $Revision: 12678 $, $Date: 2008-07-03 17:38:10 +0200 (Do, 03. Jul 2008) $
 */
public abstract class AbstractSimplePrintListener extends AbstractListener {

    private static ILogger LOG = LoggerFactory.getLogger( AbstractSimplePrintListener.class );

    private String defaultTemplateDir = "/WEB-INF/igeoportal/print";

    /**
     * @param e
     */
    @Override
    public void actionPerformed( FormEvent e ) {
        RPCWebEvent rpc = (RPCWebEvent) e;
        try {
            validate( rpc );
        } catch ( Exception ex ) {
            LOG.logError( ex.getMessage(), ex );
            gotoErrorPage( ex.getMessage() );
        }

        ViewContext vc = getViewContext( rpc );
        if ( vc == null ) {
            LOG.logError( "no valid ViewContext available; maybe your session has reached timeout limit" ); //$NON-NLS-1$
            gotoErrorPage( Messages.getString( "AbstractSimplePrintListener.MISSINGCONTEXT" ) );
            setNextPage( "igeoportal/error.jsp" );
            return;
        }
        try {
            printMap( vc, rpc );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            LOG.logError( ex.getMessage(), ex );
            gotoErrorPage( ex.getMessage() );
        }
    }

    /**
     * 
     * @param vc
     * @param rpc
     * @throws PortalException
     * @throws IOException
     * @throws SAXException
     * @throws XMLParsingException
     * @throws InconsistentRequestException
     */
    private void printMap( ViewContext vc, RPCWebEvent rpc )
                            throws PortalException, IOException, InconsistentRequestException, XMLParsingException,
                            SAXException {

        List<String> getMap = createGetMapRequests( vc );
        String image = performGetMapRequests( getMap );

        String legend = accessLegend( createLegendURLs( vc ) );

        String format = (String) rpc.getRPCMethodCall().getParameters()[0].getValue();

        RPCStruct struct = (RPCStruct) rpc.getRPCMethodCall().getParameters()[1].getValue();
        String printTemplate = (String) struct.getMember( "TEMPLATE" ).getValue();
        ServletContext sc = ( (HttpServletRequest) getRequest() ).getSession( true ).getServletContext();

        // read print template directory from defaultTemplateDir, or, if available, from the init
        // parameters
        String templateDir = getInitParameter( "TEMPLATE_DIR" );
        if ( templateDir == null ) {
            templateDir = defaultTemplateDir;
        }

        String path = sc.getRealPath( templateDir ) + '/' + printTemplate + ".jasper";
        String pathx = sc.getRealPath( templateDir ) + '/' + printTemplate + ".jrxml";
        if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
            LOG.logDebug( "The jasper template is read from: ", path );
            LOG.logDebug( "The jrxml template is read from: ", pathx );
        }

        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put( "MAP", image );
        parameter.put( "LEGEND", legend );
        
        // enable 
        if ( getInitParameter( "LOGO_URL" ) != null ){
            String logoUrl = getHomePath() + getInitParameter( "LOGO_URL" );
            if ( new File( logoUrl ).exists() ) {
                parameter.put( "LOGO_URL", logoUrl );
            }
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat( "dd.MM.yyyy", Locale.getDefault() );
        // TODO deprecated - will be remove in future versions
        parameter.put( "DATUM", sdf.format( new GregorianCalendar().getTime() ) );
        // --------------------------------------------------------
        parameter.put( "DATE", sdf.format( new GregorianCalendar().getTime() ) );
        double scale = calcScale( pathx, getMap.get( 0 ) );
        parameter.put( "MAPSCALE", "" + Math.round( scale ) );
        LOG.logDebug( "print map scale: ", scale );
        // set text area values
        RPCMember[] members = struct.getMembers();
        for ( int i = 0; i < members.length; i++ ) {
            if ( members[i].getName().startsWith( "TA:" ) ) {
                String s = members[i].getName().substring( 3, members[i].getName().length() );
                String val = (String) members[i].getValue();
                if ( val != null ) {
                    val = new String( val.getBytes(), CharsetUtils.getSystemCharset() );
                }
                LOG.logDebug( "text area name: ", s );
                LOG.logDebug( "text area value: ", val );
                parameter.put( s, val );
            }
        }

        if ( "application/pdf".equals( format ) ) {

            // create the pdf
            Object result = null;
            try {
                JRDataSource ds = new JREmptyDataSource();
                result = JasperRunManager.runReportToPdf( path, parameter, ds );
            } catch ( JRException e ) {
                LOG.logError( "Template: " + path );
                LOG.logError( e.getLocalizedMessage(), e );
                throw new PortalException( Messages.getString( "AbstractSimplePrintListener.REPORTCREATION" ) );
            } finally {
                File file = new File( image );
                file.delete();
                file = new File( legend );
                file.delete();
            }

            forwardPDF( result );

        } else if ( "image/png".equals( format ) ) {

            // create the image
            Image result = null;
            try {
                JRDataSource ds = new JREmptyDataSource();
                JasperPrint prt = JasperFillManager.fillReport( path, parameter, ds );
                result = JasperPrintManager.printPageToImage( prt, 0, 1 );
            } catch ( JRException e ) {
                LOG.logError( e.getLocalizedMessage(), e );
                throw new PortalException( Messages.getString( "AbstractSimplePrintListener.REPORTCREATION" ) );
            } finally {
                File file = new File( image );
                file.delete();
                file = new File( legend );
                file.delete();
            }

            forwardImage( result, format );

        }
    }

    private double calcScale( String path, String getmap )
                            throws InconsistentRequestException, XMLParsingException, IOException, SAXException {
        //TODO The map path is static. It should be instead read from somewhere else. 
        //A good idea would be to save the path in the web.xml of the coreesponding application,
        //or in controller.xml of the PdfPrintListener and sends it with rpc request
        
        Map<String, String> model = KVP2Map.toMap( getmap );
        model.put( "ID", "22" );
        GetMap gm = GetMap.create( model );

        File file = new File( path );
        XMLFragment xml = new XMLFragment( file.toURL() );

        String xpathW = "detail/band/image/reportElement[./@key = 'image-1']/@width";

        NamespaceContext nsc = CommonNamespaces.getNamespaceContext();
        int w = XMLTools.getRequiredNodeAsInt( xml.getRootElement(), xpathW, nsc );

        // CoordinateSystem crs = CRSFactory.create( gm.getSrs() );

        // mapsize in template in metre; templates generated by iReport are designed
        // to assume a resolution of 72dpi
        double ms = ( w / 72d ) * 0.0254;
        // TODO
        // consider no metric CRS
        return gm.getBoundingBox().getWidth() / ms;
    }

    /**
     * accesses the legend URLs passed, draws the result onto an image that are stored in a temporary file. The name of
     * the file will be returned.
     * 
     * @param legends
     * @return filename of image file
     */
    private String accessLegend( List<String[]> legends )
                            throws IOException {
        int width = Integer.parseInt( getInitParameter( "LEGENDWIDTH" ) );
        int height = Integer.parseInt( getInitParameter( "LEGENDHEIGHT" ) );
        String tmp = getInitParameter( "LEGENDBGCOLOR" );
        if ( tmp == null ) {
            tmp = "0xFFFFFF";
        }
        Color bg = Color.decode( tmp );
        BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        Graphics g = bi.getGraphics();
        g.setColor( bg );
        g.fillRect( 0, 0, bi.getWidth(), bi.getHeight() );
        g.setColor( Color.BLACK );
        int k = 10;

        for ( int i = 0; i < legends.size(); i++ ) {
            if ( k > bi.getHeight() ) {
                if ( LOG.getLevel() <= ILogger.LOG_WARNING ) {
                    LOG.logWarning( "The necessary legend size is larger than the available legend space." );
                }
            }
            String[] s = legends.get( i );
            if ( s[1] != null ) {
                LOG.logDebug( "reading legend: " + s[1] );
                Image img = null;
                try {
                    img = ImageUtils.loadImage( new URL( s[1] ) );
                } catch ( Exception e ) {
                    if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
                        String msg = StringTools.concat( 400, "Exception for Layer: ", s[0], " - ", s[1] );
                        LOG.logDebug( msg );
                        LOG.logDebug( e.getLocalizedMessage() );
                    }
                    if ( getInitParameter( "MISSING_IMAGE" ) != null ){
                        String missingImageUrl = getHomePath() + getInitParameter( "MISSING_IMAGE" );
                        File missingImage = new File( missingImageUrl );
                        if ( missingImage.exists() ) {
                            img = ImageUtils.loadImage( missingImage );
                        }
                    }                    
                }
                if ( img != null ) {
                    if ( img.getWidth( null ) < 50 ) {
                        // it is assumed that no label is assigned
                        g.drawImage( img, 0, k, null );
                        g.drawString( s[0], img.getWidth( null ) + 10, k + img.getHeight( null ) / 2 );
                    } else {
                        g.drawImage( img, 0, k, null );
                    }
                    k = k + img.getHeight( null ) + 10;
                }
            } else {
                g.drawString( "- " + s[0], 0, k + 10 );
                k = k + 20;
            }
        }
        g.dispose();

        return storeImage( bi );
    }

    /**
     * performs the GetMap requests passed, draws the result onto an image that are stored in a temporary file. The name
     * of the file will be returned.
     * 
     * @param list
     * @return filename of image file
     * @throws PortalException
     * @throws IOException
     */
    private String performGetMapRequests( List<String> list )
                            throws PortalException, IOException {

        Map<String, String> map = KVP2Map.toMap( list.get( 0 ) );
        map.put( "ID", "ww" );
        GetMap getMap = null;
        try {
            getMap = GetMap.create( map );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            String s = Messages.format( "AbstractSimplePrintListener.GETMAPCREATION", list.get( 0 ) );
            throw new PortalException( s );
        }
        BufferedImage bi = new BufferedImage( getMap.getWidth(), getMap.getHeight(), BufferedImage.TYPE_INT_ARGB );
        Graphics g = bi.getGraphics();
        for ( int i = 0; i < list.size(); i++ ) {
            URL url = new URL( list.get( i ) );
            Image img = null;
            try {
                img = ImageUtils.loadImage( url );
            } catch ( Exception e ) {
                //This is the case where a getmap request produces an error. This does not definitly mean that
                //the wms is not working, it could also be because the bounding box is not correct or too big. Try to zoom 
                // in, you might find something   
                LOG.logInfo( "could not load map from: ", url.toExternalForm() );
            }
            g.drawImage( img, 0, 0, null );
        }
        g.dispose();
        return storeImage( bi );
    }

    /**
     * stores the passed image in the defined temporary directory and returns the dynamicly created filename
     * 
     * @param bi
     * @return filename of image file
     * @throws IOException
     */
    private String storeImage( BufferedImage bi )
                            throws IOException {

        String s = UUID.randomUUID().toString();
        String tempDir = getInitParameter( "TEMPDIR" );
        if ( !tempDir.endsWith( "/" ) ) {
            tempDir = tempDir + '/';
        }
        if ( tempDir.startsWith( "/" ) ) {
            tempDir = tempDir.substring( 1, tempDir.length() );
        }
        ServletContext sc = ( (HttpServletRequest) this.getRequest() ).getSession( true ).getServletContext();
        String fileName = StringTools.concat( 300, sc.getRealPath( tempDir ), '/', s, ".png" );

        FileOutputStream fos = new FileOutputStream( new File( fileName ) );

        ImageUtils.saveImage( bi, fos, "png", 1 );
        fos.close();

        return fileName;
    }

    private void forwardPDF( Object result )
                            throws PortalException {
        // must be a byte array
        String tempDir = getInitParameter( "TEMPDIR" );
        if ( !tempDir.endsWith( "/" ) ) {
            tempDir = tempDir + '/';
        }
        if ( tempDir.startsWith( "/" ) ) {
            tempDir = tempDir.substring( 1, tempDir.length() );
        }
        ServletContext sc = ( (HttpServletRequest) this.getRequest() ).getSession( true ).getServletContext();

        String fileName = UUID.randomUUID().toString();
        String s = StringTools.concat( 200, sc.getRealPath( tempDir ), '/', fileName, ".pdf" );
        try {
            RandomAccessFile raf = new RandomAccessFile( s, "rw" );
            raf.write( (byte[]) result );
            raf.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            LOG.logError( "could not write temporary pdf file: " + s, e );
            throw new PortalException( Messages.format( "AbstractSimplePrintListener.PDFCREATION", s ), e );
        }

        getRequest().setAttribute( "PDF", StringTools.concat( 200, tempDir, fileName, ".pdf" ) );
    }

    private void forwardImage( Image result, String format )
                            throws PortalException {

        format = format.substring( format.indexOf( '/' ) + 1 );

        String tempDir = getInitParameter( "TEMPDIR" );
        if ( !tempDir.endsWith( "/" ) ) {
            tempDir = tempDir + '/';
        }
        if ( tempDir.startsWith( "/" ) ) {
            tempDir = tempDir.substring( 1, tempDir.length() );
        }
        ServletContext sc = ( (HttpServletRequest) this.getRequest() ).getSession( true ).getServletContext();

        String fileName = UUID.randomUUID().toString();
        String s = StringTools.concat( 200, sc.getRealPath( tempDir ), "/", fileName, ".", format );
        try {
            // make sure we have a BufferedImage
            if ( !( result instanceof BufferedImage ) ) {
                BufferedImage img = new BufferedImage( result.getWidth( null ), result.getHeight( null ),
                                                       BufferedImage.TYPE_INT_ARGB );

                Graphics g = img.getGraphics();
                g.drawImage( result, 0, 0, null );
                g.dispose();
                result = img;
            }

            ImageUtils.saveImage( (BufferedImage) result, s, 1 );
        } catch ( Exception e ) {
            LOG.logError( "could not write temporary pdf file: " + s, e );
            throw new PortalException( Messages.format( "AbstractSimplePrintListener.PDFCREATION", s ), e );
        }

        getRequest().setAttribute( "PDF", StringTools.concat( 200, tempDir, fileName, ".", format ) );
    }

    /**
     * fills the passed PrintMap request template with required values
     * 
     * @param vc
     * @return returns a list with all base requests
     */
    private List<String> createGetMapRequests( ViewContext vc ) {

        User user = getUser();

        // set boundingbox/envelope
        Point[] points = vc.getGeneral().getBoundingBox();

        int width = Integer.parseInt( getInitParameter( "WIDTH" ) );
        int height = Integer.parseInt( getInitParameter( "HEIGHT" ) );

        StringBuffer sb = new StringBuffer( 1000 );
        sb.append( "&BBOX=" ).append( points[0].getX() ).append( ',' );
        sb.append( points[0].getY() ).append( ',' ).append( points[1].getX() );
        sb.append( ',' ).append( points[1].getY() ).append( "&WIDTH=" );
        sb.append( width ).append( "&HEIGHT=" ).append( height );
        if ( user != null ) {
            sb.append( "&user=" ).append( user.getName() );
            sb.append( "&password=" ).append( user.getPassword() );
        }
        String[] reqs = PortalUtils.createBaseRequests( vc );
        List<String> list = new ArrayList<String>( reqs.length );
        for ( int i = 0; i < reqs.length; i++ ) {
            list.add( reqs[i] + sb.toString() );
            LOG.logDebug( "GetMap request:", reqs[i] + sb.toString() );
        }

        return list;
    }

    /**
     * returns <code>null</code> and should be overwirtten by an extending class
     * 
     * @return <code>null</code>
     */
    protected User getUser() {
        return null;
    }

    /**
     * reads the view context to print from the users session
     * 
     * @param rpc
     * @return the viewcontext defined by the rpc
     */
    abstract protected ViewContext getViewContext( RPCWebEvent rpc );

    /**
     * returns legend access URLs for all visible layers of the passed view context. If a visible layer does not define
     * a LegendURL
     * 
     * @param vc
     * @return legend access URLs for all visible layers of the passed view context. If a visible layer does not define
     *         a LegendURL
     */
    private List<String[]> createLegendURLs( ViewContext vc ) {
        Layer[] layers = vc.getLayerList().getLayers();
        List<String[]> list = new ArrayList<String[]>();
        for ( int i = 0; i < layers.length; i++ ) {
            if ( !layers[i].isHidden() ) {
                Style style = layers[i].getStyleList().getCurrentStyle();
                String[] s = new String[2];
                s[0] = layers[i].getTitle();
                if ( style.getLegendURL() != null ) {
                    s[1] = style.getLegendURL().getOnlineResource().toExternalForm();
                }
                list.add( s );
            }
        }
        return list;
    }

    /**
     * validates the incoming request/RPC if conatins all required elements
     * 
     * @param rpc
     * @throws PortalException
     */
    protected void validate( RPCWebEvent rpc )
                            throws PortalException {
        RPCStruct struct = (RPCStruct) rpc.getRPCMethodCall().getParameters()[1].getValue();
        if ( struct.getMember( "TEMPLATE" ) == null ) {
            throw new PortalException( Messages.getString( "portal.common.control.VALIDATIONERROR" ) );
        }

        if ( rpc.getRPCMethodCall().getParameters()[0].getValue() == null ) {
            throw new PortalException( Messages.getString( "portal.common.control.VALIDATIONERROR" ) );
        }
    }

}
