//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/OWSCommonCapabilities.java $
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

package org.deegree.owscommon_1_1_0;

import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;

/**
 * <code>OWSCommonCapabilities</code> encapsulates the serviceIdentification, serviceProvider and
 * the operationsMetadata representations of the ows common version 1.1.0.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10830 $, $Date: 2008-03-31 11:33:56 +0200 (Mon, 31 Mar 2008) $
 * 
 */
public class OWSCommonCapabilities extends OGCCapabilities {
    /**
     * for inter operability
     */
    private static final long serialVersionUID = 799308405149136156L;

    private final ServiceIdentification serviceIdentification;

    private final ServiceProvider serviceProvider;

    private final OperationsMetadata operationsMetadata;

    /**
     * @param version
     * @param updateSequence
     * @param serviceIdentification
     * @param serviceProvider
     * @param operationsMetadata
     */
    public OWSCommonCapabilities( String version, String updateSequence, ServiceIdentification serviceIdentification,
                                  ServiceProvider serviceProvider, OperationsMetadata operationsMetadata ) {
        super( version, updateSequence );
        this.serviceIdentification = serviceIdentification;
        this.serviceProvider = serviceProvider;
        this.operationsMetadata = operationsMetadata;

    }

    /**
     * @return the serviceIdentification.
     */
    public final ServiceIdentification getServiceIdentification() {
        return serviceIdentification;
    }

    /**
     * @return the serviceProvider.
     */
    public final ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    /**
     * @return the operationsMetadata.
     */
    public final OperationsMetadata getOperationsMetadata() {
        return operationsMetadata;
    }

}
