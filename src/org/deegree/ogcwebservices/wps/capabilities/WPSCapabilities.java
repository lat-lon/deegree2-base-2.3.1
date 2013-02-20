//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/capabilities/WPSCapabilities.java $
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
package org.deegree.ogcwebservices.wps.capabilities;

import java.io.IOException;
import java.net.URL;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.ogcwebservices.getcapabilities.Contents;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;
import org.deegree.ogcwebservices.getcapabilities.ServiceIdentification;
import org.deegree.ogcwebservices.getcapabilities.ServiceProvider;
import org.deegree.owscommon.OWSCommonCapabilities;
import org.xml.sax.SAXException;

/**
 * WPSCapabilities.java
 * 
 * Created on 08.03.2006. 18:01:22h
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * @author last edited by: $Author: ckiehle $
 *
 * @version $Revision: 10197 $, $Date: 2008-02-20 12:41:29 +0100 (Wed, 20 Feb 2008) $
 */
public class WPSCapabilities extends OWSCommonCapabilities {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7033992424153872934L;

	private static final ILogger LOG = LoggerFactory.getLogger( WPSCapabilities.class );

    /**
     * List of brief descriptions of the processes offered by this WPS server.
     */
    private ProcessOfferings processOfferings;

    /**
     * 
     * @param version
     * @param updateSequence
     * @param serviceIdentification
     * @param serviceProvider
     * @param operationsMetadata
     * @param contents
     */
    protected WPSCapabilities( String version, String updateSequence,
                               ServiceIdentification serviceIdentification,
                               ServiceProvider serviceProvider,
                               OperationsMetadata operationsMetadata, Contents contents ) {
        super( version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata,
               contents );
        LOG.logInfo( "WPSCapabilities instance created." );
    }

    /**
     * 
     * @param url
     * @return capabilities
     * @throws IOException
     * @throws SAXException
     * @throws InvalidCapabilitiesException
     */
    public static OGCCapabilities createCapabilities( URL url )
                            throws IOException, SAXException, InvalidCapabilitiesException {
        OGCCapabilities capabilities = null;
        WPSCapabilitiesDocument capabilitiesDoc = new WPSCapabilitiesDocument();
        capabilitiesDoc.load( url );
        capabilities = capabilitiesDoc.parseCapabilities();
        return capabilities;
    }

    /**
     * @return Returns the processOfferings.
     */
    public ProcessOfferings getProcessOfferings() {
        return processOfferings;
    }

    /**
     * @param processOfferings
     *            The processOfferings to set.
     */
    public void setProcessOfferings( ProcessOfferings processOfferings ) {
        this.processOfferings = processOfferings;
    }

}
