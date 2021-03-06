//$$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/configuration/WPVSDeegreeParams.java $$
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

package org.deegree.ogcwebservices.wpvs.configuration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.deegree.enterprise.DeegreeParams;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.ImageUtils;
import org.deegree.model.metadata.iso19115.OnlineResource;

/**
 * 
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: rbezema $
 * 
 * $Revision: 10460 $, $Date: 2008-03-05 17:48:34 +0100 (Mi, 05. Mär 2008) $
 * 
 */
public class WPVSDeegreeParams extends DeegreeParams {

    /**
     * 
     */
    private static final long serialVersionUID = 4480667559177855009L;

    private static final ILogger LOG = LoggerFactory.getLogger( WPVSDeegreeParams.class );

    private String copyright; // copyright is either text-string or url-string

    // the configured copyrightImage to paint over the GetView response
    private BufferedImage copyrightImage = null;

    private float viewQuality = 0.95f;

    private int maxLifeTime = 3600;

    private final Map<String, URL> backgroundMap;

    private final boolean isWatermarked;

    private final int maxViewWidth;

    private final int maxViewHeight;

    private final boolean requestQualityPreferred;

    private double maximumFarClippingPlane;

    private String defaultSplitter;

    private double minimalTerrainHeight;

    private final double minimalWCS_DGMResolution;

    private double extendRequestPercentage;

    private double nearClippingPlane;

    private final int maxTextureDimension;

    private int quadMergeCount;

    /**
     * 
     * 
     * @param defaultOnlineResource
     * @param cacheSize
     * @param requestTimeLimit
     * @param characterSet
     * @param copyright
     * @param watermarked
     * @param maxLifeTime
     * @param viewQuality
     * @param backgroundMap
     *            a <code>Map</code> for background images. This Map contains image names as keys and image URL as
     *            values
     * @param maxWidth
     * @param maxHeight
     * @param requestQualityPreferred
     * @param maximumFarClippingPlane
     *            to which extend the request can set a farclippingplane
     * @param nearClippingPlane
     *            of the viewport
     * @param defaultSplitter
     *            What kind of splitter to use if the GetView request has no field named "splitter"
     * @param minimalTerrainHeight
     *            the minimalheight of a terrain if no dgm is found.
     * @param minimalWCS_DGMResolution
     *            the configured minimal resolution of a wcs dgm
     * @param extendRequestPercentage
     *            the percentage to which wcs/wms request should be extended.
     * @param maxTextureDimension
     *            the maximums request size of the textures
     * @param quadMergeCount
     *            a value for the quadtree-splitter to tell at which amount the leaves should be merged.
     */
    public WPVSDeegreeParams( OnlineResource defaultOnlineResource, int cacheSize, int requestTimeLimit,
                              String characterSet, String copyright, boolean watermarked, int maxLifeTime,
                              float viewQuality, Map<String, URL> backgroundMap, int maxWidth, int maxHeight,
                              boolean requestQualityPreferred, double maximumFarClippingPlane,
                              double nearClippingPlane, String defaultSplitter, double minimalTerrainHeight,
                              double minimalWCS_DGMResolution, double extendRequestPercentage, int maxTextureDimension,
                              int quadMergeCount ) {

        super( defaultOnlineResource, cacheSize, requestTimeLimit, characterSet );

        this.copyright = copyright;
        this.maxLifeTime = maxLifeTime;
        this.viewQuality = viewQuality;
        this.backgroundMap = backgroundMap;
        this.isWatermarked = watermarked;
        this.maxViewWidth = maxWidth;
        this.maxViewHeight = maxHeight;
        this.requestQualityPreferred = requestQualityPreferred;
        this.maximumFarClippingPlane = maximumFarClippingPlane;
        this.nearClippingPlane = nearClippingPlane;
        this.minimalWCS_DGMResolution = minimalWCS_DGMResolution;
        this.defaultSplitter = defaultSplitter.toUpperCase();
        if ( !( "QUAD".equals( defaultSplitter ) || "BBOX".equals( defaultSplitter ) ) ) {
            LOG.logWarning( "The configured defaultSplitter does not exist, setting to QUAD" );
            this.defaultSplitter = "QUAD";
        }

        if ( copyright != null && !"".equals( copyright.trim() ) ) {
            try {
                copyrightImage = ImageUtils.loadImage(  new URL( this.copyright ).getFile() );
            } catch ( MalformedURLException murle ) {
                // The Copyright is a String.
            } catch ( IOException ioe ) {
                // The Copyright is a String.
            }
        }
        this.minimalTerrainHeight = minimalTerrainHeight;
        this.extendRequestPercentage = extendRequestPercentage;
        this.maxTextureDimension = maxTextureDimension;
        if ( quadMergeCount < 0 ) {
            quadMergeCount = 0;
        }
        this.quadMergeCount = quadMergeCount;
    }

    /**
     * @return Returns the copyright.
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * @return Returns the maxLifeTime.
     */
    public int getMaxLifeTime() {
        return maxLifeTime;
    }

    /**
     * @return Returns the viewQuality.
     */
    public float getViewQuality() {
        return viewQuality;
    }

    /**
     * @return the configured different backgroundimages
     */
    public Map<String, URL> getBackgroundMap() {
        return backgroundMap;
    }

    /**
     * @return true if the image should be watermarked (with an image or text)
     */
    public boolean isWatermarked() {
        return isWatermarked;
    }

    /**
     * @return maximum value of the width of a request
     */
    public int getMaxViewWidth() {
        return maxViewWidth;
    }

    /**
     * @return maximum value of the height of a request
     */
    public int getMaxViewHeight() {
        return maxViewHeight;
    }

    /**
     * @return true if the splitter should use hight quality (lot of request quads) or false otherwise.
     */
    public boolean isRequestQualityPreferred() {
        return requestQualityPreferred;
    }

    /**
     * @return the maximumFarClippingPlane which a user can request.
     */
    public double getMaximumFarClippingPlane() {
        return maximumFarClippingPlane;
    }

    /**
     * @return the copyrightImage as a BufferedImage.
     */
    public BufferedImage getCopyrightImage() {
        return copyrightImage;
    }

    /**
     * @return the defaultSplitter.
     */
    public String getDefaultSplitter() {
        return defaultSplitter;
    }

    /**
     * @return the minimalTerrainHeight which is used as zValue of a terrain if no dgm is found (configured) for a
     *         request. The default value is 0d.
     */
    public double getMinimalTerrainHeight() {
        return minimalTerrainHeight;
    }

    /**
     * @return the minimalWCS_DGMResolution.
     */
    public double getMinimalWCS_DGMResolution() {
        return minimalWCS_DGMResolution;
    }

    /**
     * @return the percentage to which wcs-request should be extended
     */
    public double getExtendRequestPercentage() {
        return extendRequestPercentage;
    }

    /**
     * @return the configured nearclipping plane.
     */
    public double getNearClippingPlane() {
        return nearClippingPlane;
    }

    /**
     * @return the maxim dimension of a requested Texture.
     */
    public final int getMaxTextureDimension() {
        return maxTextureDimension;
    }

    /**
     * @return the number of leaves a quadtree-splitter can have before it starts merging leaves together.
     */
    public final int getQuadMergeCount() {
        return quadMergeCount;
    }
}