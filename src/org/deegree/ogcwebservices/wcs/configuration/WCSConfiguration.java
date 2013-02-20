// $HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/configuration/WCSConfiguration.java $

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
package org.deegree.ogcwebservices.wcs.configuration;

import java.io.IOException;
import java.net.URL;

import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.getcapabilities.Capability;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.getcapabilities.Service;
import org.deegree.ogcwebservices.wcs.getcapabilities.ContentMetadata;
import org.deegree.ogcwebservices.wcs.getcapabilities.WCSCapabilities;
import org.xml.sax.SAXException;

/**
 * @version $Revision: 10679 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10679 $, $Date: 2008-03-25 18:26:57 +0100 (Tue, 25 Mar 2008) $
 */

public class WCSConfiguration extends WCSCapabilities {

    private WCSDeegreeParams deegreeParams = null;

    /**
     * creates a WCSConfiguration from an URL enabling access to a deegree WCS configuration
     * document
     * 
     * @param url
     * @return a WCSConfiguration
     * @throws IOException
     * @throws SAXException
     */
    public static WCSConfiguration create( URL url )
                            throws IOException, SAXException, InvalidCapabilitiesException,
                            InvalidConfigurationException {
        WCSConfigurationDocument confDoc = new WCSConfigurationDocument();
        confDoc.load( url );
        return new WCSConfiguration( confDoc );
    }

    /**
     * creates a WCSConfiguration from a deegree WCSConfigurationDocument
     * 
     * @param confDoc
     * @throws InvalidCapabilitiesException
     * @throws InvalidConfigurationException
     */
    private WCSConfiguration( WCSConfigurationDocument confDoc ) throws InvalidCapabilitiesException,
                            InvalidConfigurationException {
        super( confDoc.parseVersion(), null, confDoc.parseServiceSection(),
               confDoc.getCapabilitySection( CommonNamespaces.WCSNS ), confDoc.parseContentMetadataSection() );
        this.deegreeParams = confDoc.getDeegreeParamsSection();
    }

    /**
     * creates a WCSConfiguration from its sections
     * 
     * @param version
     * @param updateSequence
     * @param service
     * @param capabilitiy
     * @param contentMetadata
     */
    public WCSConfiguration( String version, String updateSequence, WCSDeegreeParams deegreeParams, Service service,
                             Capability capabilitiy, ContentMetadata contentMetadata ) {
        super( version, updateSequence, service, capabilitiy, contentMetadata );
        this.deegreeParams = deegreeParams;
    }

    /**
     * @return Returns the deegreeParam.
     * 
     */
    public WCSDeegreeParams getDeegreeParams() {
        return deegreeParams;
    }

    /**
     * @param deegreeParams
     *            The deegreeParam to set.
     */
    public void setDeegreeParam( WCSDeegreeParams deegreeParams ) {
        this.deegreeParams = deegreeParams;
    }

}
