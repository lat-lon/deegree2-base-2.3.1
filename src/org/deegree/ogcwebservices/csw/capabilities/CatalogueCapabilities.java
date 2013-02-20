//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/capabilities/CatalogueCapabilities.java $
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
package org.deegree.ogcwebservices.csw.capabilities;

import java.io.IOException;
import java.net.URL;

import org.deegree.model.filterencoding.capabilities.FilterCapabilities;
import org.deegree.ogcwebservices.getcapabilities.Contents;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;
import org.deegree.ogcwebservices.getcapabilities.ServiceIdentification;
import org.deegree.ogcwebservices.getcapabilities.ServiceProvider;
import org.deegree.owscommon.OWSCommonCapabilities;
import org.xml.sax.SAXException;

/**
 * Represents the capabilities for an OGC-CSW 2.0.0 compliant service instance.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 */

public class CatalogueCapabilities extends OWSCommonCapabilities {

    /**
     * 
     */
    private static final long serialVersionUID = 1351565525170636799L;

    private FilterCapabilities filterCapabilities;

    private EBRIMCapabilities ebrimCaps;

    /**
     * Creates catalog capabilities from a URL.
     * 
     * @param url
     *            location of the capabilities file
     * @return catalog capabilities
     * @throws IOException
     * @throws SAXException
     * @throws InvalidCapabilitiesException
     */
    public static OGCCapabilities createCapabilities( URL url )
                            throws IOException, SAXException, InvalidCapabilitiesException {
        OGCCapabilities capabilities = null;
        CatalogueCapabilitiesDocument capabilitiesDoc = new CatalogueCapabilitiesDocument();
        capabilitiesDoc.load( url );
        capabilities = capabilitiesDoc.parseCapabilities();
        return capabilities;
    }

    /**
     * Generates a new CatalogCapabilities instance from the given parameters.
     * 
     * @param version
     * @param updateSequence
     * @param serviceIdentification
     * @param serviceProvider
     * @param operationsMetadata
     * @param contents
     * @param filterCapabilities
     */
    public CatalogueCapabilities( String version, String updateSequence,
                                  ServiceIdentification serviceIdentification,
                                  ServiceProvider serviceProvider,
                                  OperationsMetadata operationsMetadata, Contents contents,
                                  FilterCapabilities filterCapabilities ) {
        super( version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata,
               contents );
        this.filterCapabilities = filterCapabilities;
    }

    /**
     * Generates a new CatalogCapabilities instance from the given parameters.
     * 
     * @param version
     * @param updateSequence
     * @param serviceIdentification
     * @param serviceProvider
     * @param operationsMetadata
     * @param contents
     * @param filterCapabilities
     * @param ebrimCaps
     *            the specified ebrim extensions
     */
    public CatalogueCapabilities( String version, String updateSequence,
                                  ServiceIdentification serviceIdentification,
                                  ServiceProvider serviceProvider,
                                  OperationsMetadata operationsMetadata, Contents contents,
                                  FilterCapabilities filterCapabilities, EBRIMCapabilities ebrimCaps ) {
        super( version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata,
               contents );
        this.filterCapabilities = filterCapabilities;
        this.ebrimCaps = ebrimCaps;
    }

    /**
     * Returns the FilterCapabilites section of the capabilities.
     * 
     * @return the FilterCapabilites section of the capabilities
     */
    public FilterCapabilities getFilterCapabilities() {
        return filterCapabilities;
    }

    /**
     * @return the ebrimCaps specified by the ogc ebrim-extension.
     */
    public EBRIMCapabilities getEbrimCaps() {
        return ebrimCaps;
    }

}