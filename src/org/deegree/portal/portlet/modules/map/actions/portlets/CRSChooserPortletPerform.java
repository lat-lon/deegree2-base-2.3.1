//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/portlet/modules/map/actions/portlets/CRSChooserPortletPerform.java $
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

package org.deegree.portal.portlet.modules.map.actions.portlets;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.jetspeed.portal.Portlet;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Point;
import org.deegree.portal.PortalException;
import org.deegree.portal.context.ViewContext;
import org.deegree.portal.portlet.modules.actions.AbstractPortletPerform;
import org.deegree.portal.portlet.modules.actions.IGeoPortalPortletPerform;

/**
 * This Perform class takes care of changing the WMC's bounding box based on a scale paramter. The parameter is passed
 * in the request. The paramter name is defined by the static member NEW_SCALE_VALUE
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: otonnhofer $
 * 
 * @version $Revision: 10708 $, $Date: 2008-03-26 15:53:31 +0100 (Wed, 26 Mar 2008) $
 */

public class CRSChooserPortletPerform extends IGeoPortalPortletPerform {

    private static final ILogger LOG = LoggerFactory.getLogger( CRSChooserPortletPerform.class );

    public static final String REQUESTED_CRS = "REQUESTED_CRS";

    public static final String AVAILABLE_CRS = "AVAILABLE_CRS";

    public static final String AVAILABLE_CRS_NAMES = "AVAILABLE_CRS_NAMES";

    private static Map<String, Envelope> homeBBox;
    static {
        if ( homeBBox == null ) {
            homeBBox = new HashMap<String, Envelope>();
        }
    }

    /**
     * private constructor
     * 
     * @param request
     * @param portlet
     * @param sc
     */
    CRSChooserPortletPerform( HttpServletRequest request, Portlet portlet, ServletContext sc ) {
        super( request, portlet, sc );
    }

    /**
     * TODO reads the init parameters of the portlet and build the scale list
     * 
     * @throws PortalException
     * 
     */
    void readInitParameter()
                            throws PortalException {

        HttpSession ses = request.getSession();
        if ( ses.getAttribute( AVAILABLE_CRS ) == null ) {
            String list = getInitParam( AVAILABLE_CRS );
            if ( list == null ) {
                list = "EPSG:31467|GK 3;EPSG:4326|WGS 84";
            }
            String[] tmp = list.split( ";" );
            String[] crs = new String[tmp.length];
            Map<String, String> crsToNames = new HashMap<String, String>( crs.length );

            for ( int i = 0; i < tmp.length; i++ ) {
                String[] tmpEntry = tmp[i].split( "\\|" );
                if ( tmpEntry.length != 2 ) {
                    throw new PortalException( "Parameter is not properly define. An entry must contain a CRS and a "
                                               + "name, and they must be separeted by a '|'" );
                }
                crs[i] = tmpEntry[0];
                crsToNames.put( crs[i], tmpEntry[1] );
            }

            ses.setAttribute( AVAILABLE_CRS, crs );
            ses.setAttribute( AVAILABLE_CRS_NAMES, crsToNames );
        }

    }

    /**
     * Changes the CRS of the current ViewContext. The CRS value is in the parameter under the 'REQUESTED_CRS' key.
     * 
     * @throws PortalException
     * 
     */
    void doCRSChange()
                            throws PortalException {

        String newCRS = parameter.get( REQUESTED_CRS );

        if ( newCRS == null ) {
            // throw new PortalException( "No crs available in the request. Missing " +
            // REQUESTED_CRS + " parameter" );
            return;
        }

        ViewContext vc = getCurrentViewContext( getInitParam( INIT_MAPPORTLETID ) );
        if ( vc == null ) {
            throw new PortalException( "no valid view context available through users session" );
        }

        // read points/bbox from ViewCOntext
        Point p0 = vc.getGeneral().getBoundingBox()[0];
        Point p1 = vc.getGeneral().getBoundingBox()[1];

        CoordinateSystem oldCs = p0.getCoordinateSystem();

        if ( oldCs.getPrefixedName().equals( newCRS ) ) {
            return;
        }
        

        // transform home bbox to new CRS
        Envelope homeEnv = (Envelope) request.getSession().getAttribute( AbstractPortletPerform.SESSION_HOME );
        if ( homeBBox.get( oldCs.getPrefixedName() ) == null ) {
            homeBBox.put( oldCs.getPrefixedName(), homeEnv );
        }
        try {
            GeoTransformer trans = new GeoTransformer( newCRS );
            
            if ( homeBBox.get( newCRS ) == null ) {
                homeEnv = trans.transform( homeEnv, oldCs );
                homeEnv = ensureAspectRatio( homeEnv, vc );
                homeBBox.put( newCRS, homeEnv );
            } else {
                homeEnv = homeBBox.get( newCRS );
            }
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new PortalException( e.getMessage(), e );
        }
        request.getSession().setAttribute( AbstractPortletPerform.SESSION_HOME, homeEnv );

        try {
            CoordinateSystem newCs = CRSFactory.create( newCRS );

            Envelope env = GeometryFactory.createEnvelope( p0.getX(), p0.getY(), p1.getX(), p1.getY(), oldCs );

            GeoTransformer gt = new GeoTransformer( newCs );
            env = gt.transform( env, oldCs );
            env = ensureAspectRatio( env, vc );

            // set new bbox
            p0 = GeometryFactory.createPoint( env.getMin().getX(), env.getMin().getY(), newCs );
            p1 = GeometryFactory.createPoint( env.getMax().getX(), env.getMax().getY(), newCs );

            vc.getGeneral().setBoundingBox( new Point[] { p0, p1 } );

            setCurrentMapContext( vc, getInitParam( INIT_MAPPORTLETID ) );

        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new PortalException( e.getMessage(), e );
        }
    }

    private Envelope ensureAspectRatio( Envelope env, ViewContext vc ) {
        Rectangle rect = vc.getGeneral().getWindow();
        double ar = ( (double) rect.width ) / ( (double) rect.height );
        double envAr = env.getWidth() / env.getHeight();
        if ( ar < envAr ) {
            double tmp = env.getWidth() / ar;
            double miny = env.getCentroid().getY() - tmp / 2d;
            double maxy = env.getCentroid().getY() + tmp / 2d;
            env = GeometryFactory.createEnvelope( env.getMin().getX(), miny, env.getMax().getX(), maxy,
                                                  env.getCoordinateSystem() );
        } else if ( ar > envAr ) {
            double tmp = env.getHeight() * ar;
            double minx = env.getCentroid().getX() - tmp / 2d;
            double maxx = env.getCentroid().getX() + tmp / 2d;
            env = GeometryFactory.createEnvelope( minx, env.getMin().getY(), maxx, env.getMax().getY(),
                                                  env.getCoordinateSystem() );
        }
        return env;
    }

}