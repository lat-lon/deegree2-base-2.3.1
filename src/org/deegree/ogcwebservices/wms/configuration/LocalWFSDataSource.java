//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wms/configuration/LocalWFSDataSource.java $
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

import org.deegree.datatypes.QualifiedName;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.ogcwebservices.OGCWebService;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wfs.WFService;
import org.deegree.ogcwebservices.wfs.WFServiceFactory;
import org.deegree.ogcwebservices.wfs.configuration.WFSConfiguration;
import org.deegree.ogcwebservices.wfs.operation.Query;
import org.deegree.ogcwebservices.wms.capabilities.ScaleHint;

/**
 * Data source description for a LOCALWFS datasource
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 */
public class LocalWFSDataSource extends AbstractDataSource {

    private Query query = null;

    private QualifiedName geometryProperty = null;

    /**
     * Creates a new DataSource object.
     * @param querable 
     * @param failOnException 
     * 
     * @param name
     *            name of the featuretype to access
     * @param type
     *            type of the data source (REMOTEWCS, LOCALWCS)
     * @param geometryProperty 
     * @param ows
     *            <tt>OGCWebService</tt> instance for accessing the data source
     * @param capabilitiesURL 
     * @param scaleHint
     *            filter condition
     * @param validArea 
     * @param query 
     * @param featureInfoTransform 
     * @param reqTimeLimit 
     */
    public LocalWFSDataSource( boolean querable, boolean failOnException, QualifiedName name,
                              int type, QualifiedName geometryProperty, OGCWebService ows,
                              URL capabilitiesURL, ScaleHint scaleHint, Geometry validArea,
                              Query query, URL featureInfoTransform, int reqTimeLimit ) {
        super( querable, failOnException, name, type, ows, capabilitiesURL, scaleHint, validArea,
               featureInfoTransform, reqTimeLimit );
        this.query = query;
        this.geometryProperty = geometryProperty;
    }

    /**
     * @return the WFS Query that describes the access/filtering to the data source.
     */
    public Query getQuery() {
        return query;
    }

    /**
     * @return the name of the geometry property in case the datasource is of type LOCALWFS /
     * REMOTEWFS.
     * <p>
     * 
     */
    public QualifiedName getGeometryProperty() {
        return geometryProperty;
    }

    /**
     * Returns an instance of the <tt>OGCWebService</tt> that represents the datasource.
     * 
     * TODO if more than one layer uses data that are offered by the same OWS the deegree WMS shall
     * just use one instance for accessing the OWS.
     */
    @Override
    public OGCWebService getOGCWebService() throws OGCWebServiceException {
        // not sure why new services are recreated always
        return WFServiceFactory.createInstance( (WFSConfiguration) ( (WFService) ows ).getCapabilities() );
//        return ows;
    }

}