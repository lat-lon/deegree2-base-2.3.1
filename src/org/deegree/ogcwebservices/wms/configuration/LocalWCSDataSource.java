//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wms/configuration/LocalWCSDataSource.java $
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

import java.awt.Color;
import java.net.URL;

import org.deegree.datatypes.QualifiedName;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.ogcwebservices.OGCWebService;
import org.deegree.ogcwebservices.wcs.WCService;
import org.deegree.ogcwebservices.wcs.configuration.WCSConfiguration;
import org.deegree.ogcwebservices.wcs.getcoverage.GetCoverage;
import org.deegree.ogcwebservices.wms.capabilities.ScaleHint;

/**
 * Data source description for a LOCALWCS datasource
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public class LocalWCSDataSource extends AbstractDataSource {

    private GetCoverage getCoverage = null;

    private Color[] transparentColors = null;

    /**
     * Creates a new DataSource object.
     * 
     * @param querable
     * @param failOnException
     * 
     * @param name
     *            name of the featuretype to access
     * @param type
     *            type of the data source (REMOTEWCS, LOCALWCS)
     * @param ows
     *            <tt>OGCWebService</tt> instance for accessing the data source
     * @param capabilitiesURL
     * @param scaleHint
     * @param validArea
     * @param getCoverage
     *            filter condition
     * @param transparentColors
     * @param reqTimeLimit
     */
    public LocalWCSDataSource( boolean querable, boolean failOnException, QualifiedName name, int type,
                               OGCWebService ows, URL capabilitiesURL, ScaleHint scaleHint, Geometry validArea,
                               GetCoverage getCoverage, Color[] transparentColors, int reqTimeLimit ) {
        super( querable, failOnException, name, type, ows, capabilitiesURL, scaleHint, validArea, null, reqTimeLimit );
        this.getCoverage = getCoverage;
        this.transparentColors = transparentColors;
    }

    /**
     * returns an instance of a <tt>WCSGetCoverageRequest</tt> encapsulating the filter conditions
     * against a remote WCS. The request object contains: VERSION, LAYER, FORMAT,
     * VENDORSPECIFICPARAMETERS
     * 
     * @return filter conditions
     * 
     */
    public GetCoverage getGetCoverageRequest() {
        return getCoverage;
    }

    /**
     * @return an array of colors that shall be treated as transparent
     */
    public Color[] getTransparentColors() {
        return transparentColors;
    }

    /**
     * @return an instance of the <tt>OGCWebService</tt> that represents the datasource. Notice:
     *         if more than one layer uses data that are offered by the same OWS the deegree WMS
     *         shall just use one instance for accessing the OWS
     * 
     */
    @Override
    public OGCWebService getOGCWebService() {
        return new WCService( (WCSConfiguration) ows.getCapabilities() );
    }
}
