//$HeadURL: $
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


package org.deegree.ogcwebservices.wcts.configuration;

import org.deegree.ogcwebservices.wcts.capabilities.Content;
import org.deegree.ogcwebservices.wcts.capabilities.WCTSCapabilities;
import org.deegree.owscommon_1_1_0.OperationsMetadata;
import org.deegree.owscommon_1_1_0.ServiceIdentification;
import org.deegree.owscommon_1_1_0.ServiceProvider;

/**
 * <code>WCTSConfiguration</code> TODO add documentation here
 *
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 *
 * @author last edited by: $Author:$
 *
 * @version $Revision:$, $Date:$
 *
 */
public class WCTSConfiguration extends WCTSCapabilities{

    /**
     * 
     */
    private static final long serialVersionUID = 3906808063695620386L;
    private final WCTSDeegreeParams deegreeParams;
    
    

    /**
     * @param version
     * @param updateSequence
     * @param serviceIdentification
     * @param serviceProvider
     * @param operationsMetadata
     * @param contents
     * @param deegreeParams which will hold this service specific parameters.
     */
    public WCTSConfiguration( String version, String updateSequence, ServiceIdentification serviceIdentification,
                              ServiceProvider serviceProvider, OperationsMetadata operationsMetadata, Content contents, WCTSDeegreeParams deegreeParams) {
        super( version, updateSequence, serviceIdentification, serviceProvider, operationsMetadata, contents );
        this.deegreeParams = deegreeParams;
    }

    /**
     * @param capabilities
     * @param deegreeParams
     */
    public WCTSConfiguration( WCTSCapabilities capabilities, WCTSDeegreeParams deegreeParams ) {
        super( capabilities );
        this.deegreeParams = deegreeParams;
    }

    /**
     * @return the deegreeParams.
     */
    public final WCTSDeegreeParams getDeegreeParams() {
        return deegreeParams;
    }
    

}

