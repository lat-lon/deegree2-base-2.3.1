//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wms/configuration/WMSDeegreeParams.java $
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
package org.deegree.ogcwebservices.wms.configuration;

import java.net.URL;
import java.util.List;

import org.deegree.enterprise.DeegreeParams;
import org.deegree.enterprise.Proxy;
import org.deegree.model.metadata.iso19115.OnlineResource;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 12164 $
 */
public class WMSDeegreeParams extends DeegreeParams {

    private static final long serialVersionUID = -8725187234309726943L;

    private float mapQuality = 0.95f;

    private int maxLifeTime = 3600;

    private int maxMapWidth = 1000;

    private int maxMapHeight = 1000;

    private String copyRight = "";

    private URL schemaLocation = null;

    private URL dtdLocation = null;

    private Proxy proxy = null;

    private boolean antiAliased = false;

    private int featureInfoRadius = 5;

    private List<String> supportedVersions;

    private URL featureSchemaLocation;

    private String featureSchemaNamespace;

    private boolean filtersAllowed;

    /**
     * 
     * @param cacheSize
     * @param maxLifeTime
     * @param requestTimeLimit
     * @param mapQuality
     * @param defaultOnlineResource
     * @param maxMapWidth
     * @param maxMapHeight
     * @param antiAliased
     * @param featureInfoRadius
     *            (default = 5)
     * @param copyRight
     * @param schemaLocation
     * @param dtdLocation
     * @param proxy
     * @param supportedVersions
     * @param featureSchemaLocation
     * @param featureSchemaNamespace
     * @param filtersAllowed
     */
    public WMSDeegreeParams( int cacheSize, int maxLifeTime, int requestTimeLimit, float mapQuality,
                             OnlineResource defaultOnlineResource, int maxMapWidth, int maxMapHeight,
                             boolean antiAliased, int featureInfoRadius, String copyRight, URL schemaLocation,
                             URL dtdLocation, Proxy proxy, List<String> supportedVersions, URL featureSchemaLocation,
                             String featureSchemaNamespace, boolean filtersAllowed ) {
        super( defaultOnlineResource, cacheSize, requestTimeLimit );
        this.maxLifeTime = maxLifeTime;
        this.mapQuality = mapQuality;
        this.maxMapHeight = maxMapHeight;
        this.maxMapWidth = maxMapWidth;
        this.antiAliased = antiAliased;
        this.copyRight = copyRight;
        this.schemaLocation = schemaLocation;
        this.dtdLocation = dtdLocation;
        this.proxy = proxy;
        this.featureInfoRadius = featureInfoRadius;
        this.supportedVersions = supportedVersions;
        this.featureSchemaLocation = featureSchemaLocation;
        this.featureSchemaNamespace = featureSchemaNamespace;
        this.filtersAllowed = filtersAllowed;
    }

    /**
     * @return the maximum life time of the internal processes (Threads) of the deegree WMS. default
     *         is 3600 seconds. Datasources that are linked to WMS are not targeted by this value.
     * 
     * 
     */
    public int getMaxLifeTime() {
        return maxLifeTime;
    }

    /**
     * @return a copy right note to draw at the left side of the maps bottom
     * 
     */
    public String getCopyRight() {
        return copyRight;
    }

    /**
     * @return the quality of the map for none loss-less image formats. the value ranges from 0
     *         (lowest quality) to 1 (best quality)
     *         <p>
     *         Default is 0.95
     */
    public float getMapQuality() {
        return mapQuality;
    }

    /**
     * @return the maximum map height that can be requested. If the GetMap-Parameter 'HEIGHT'
     *         extends max map width an exception shall be returned to the client.
     *         <p>
     *         Default is 1000
     */
    public int getMaxMapHeight() {
        return maxMapHeight;
    }

    /**
     * @return the maximum map width that can be requested. If the GetMap-Parameter 'WIDTH' extends
     *         max map width an exception shall be returned to the client.
     *         <p>
     *         Default is 1000
     */
    public int getMaxMapWidth() {
        return maxMapWidth;
    }

    /**
     * @return the URL where the sxm schema definition of the response to an GetFeatureInfo request
     *         is located
     */
    public URL getSchemaLocation() {
        return schemaLocation;
    }

    /**
     * @return the URL where the DTD defining the OGC WMS capabilities is located
     */
    public URL getDTDLocation() {
        return dtdLocation;
    }

    /**
     * @return the proxy used with the WMS.
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * @return true if a map shall be rendered with antialising
     */
    public boolean isAntiAliased() {
        return antiAliased;
    }

    /**
     * @return the radius (pixel) of the area considered for a feature info request (default = 5px)
     */
    public int getFeatureInfoRadius() {
        return featureInfoRadius;
    }

    /**
     * @return the supportedVersions.
     */
    public List<String> getSupportedVersions() {
        return supportedVersions;
    }

    /**
     * @param maxMapHeight
     *            The maxMapHeight to set.
     */
    public void setMaxMapHeight( int maxMapHeight ) {
        this.maxMapHeight = maxMapHeight;
    }

    /**
     * @param maxMapWidth
     *            The maxMapWidth to set.
     */
    public void setMaxMapWidth( int maxMapWidth ) {
        this.maxMapWidth = maxMapWidth;
    }

    /**
     * @return Returns the featureSchemaLocation.
     */
    public URL getFeatureSchemaLocation() {
        return featureSchemaLocation;
    }

    /**
     * @return Returns the featureSchemaNamespace.
     */
    public String getFeatureSchemaNamespace() {
        return featureSchemaNamespace;
    }

    /**
     * @return whether filters are supported by this WMS
     */
    public boolean getFiltersAllowed() {
        return filtersAllowed;
    }

}
