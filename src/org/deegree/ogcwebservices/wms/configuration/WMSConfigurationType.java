//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wms/configuration/WMSConfigurationType.java $
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

import org.deegree.ogcwebservices.wms.capabilities.Layer;
import org.deegree.owscommon_new.OperationsMetadata;
import org.deegree.owscommon_new.ServiceIdentification;

/**
 * <code>WMSConfigurationType</code> defines the methods that each WMS configuration object has to
 * implement. It is used to unify the implementations for the different versions.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */

public interface WMSConfigurationType {

    /**
     * @return Returns the deegreeParams.
     */
    public WMSDeegreeParams getDeegreeParams();

    /**
     * @param deegreeParams
     *            The deegreeParams to set.
     */
    public void setDeegreeParams( WMSDeegreeParams deegreeParams );

    /**
     * @return Gets the base URL which is used to resolve file resource (XSL sheets).
     */
    public URL getBaseURL();

    /**
     * @return the version
     */
    public String getVersion();

    /**
     * @return the <code>ServiceIdentification</code> object
     */
    public ServiceIdentification getServiceIdentification();

    /**
     * @return the updateSequence.
     */
    public String getUpdateSequence();

    /**
     * 
     * @return the root layer provided by a WMS
     */
    public Layer getLayer();

    /**
     * 
     * @param name
     *            the layer name
     * @return the layer provided by a WMS or null, if there is no such layer
     */
    public Layer getLayer( String name );

    /**
     * @return the operations metadata object
     */
    public OperationsMetadata getOperationMetadata();

    /**
     * 
     * @param version
     *            the input version
     * @return the nearest supported version according to the version negotiation rules.
     */
    public String calculateVersion( String version );

    /**
     * @return the list of available exceptions
     */
    public List<String> getExceptions();

}
