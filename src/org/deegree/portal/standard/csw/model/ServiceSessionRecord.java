//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/csw/model/ServiceSessionRecord.java $
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

package org.deegree.portal.standard.csw.model;

import java.io.Serializable;

/**
 * A <code>${type_name}</code> class.<br/> TODO class description
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public class ServiceSessionRecord extends SessionRecord implements Serializable {

    private static final long serialVersionUID = 4619280068827133683L;

    private String serviceAddress;

    private String serviceType;

    private String serviceTypeVersion;

    /**
     * @param identifier
     * @param catalogName
     * @param title
     */
    public ServiceSessionRecord( String identifier, String catalogName, String title ) {
        super( identifier, catalogName, title );
        this.serviceAddress = null;
        this.serviceType = null;
        this.serviceTypeVersion = null;
    }

    /**
     * @param identifier
     * @param catalogName
     * @param title
     * @param serviceAddress
     * @param serviceType
     * @param serviceTypeVersion
     */
    public ServiceSessionRecord( String identifier, String catalogName, String title,
                                String serviceAddress, String serviceType, String serviceTypeVersion ) {
        super( identifier, catalogName, title );
        this.serviceAddress = serviceAddress;
        this.serviceType = serviceType;
        this.serviceTypeVersion = serviceTypeVersion;
    }

    /**
     * @param ssr
     */
    public ServiceSessionRecord( ServiceSessionRecord ssr ) {
        super( ssr );
        this.serviceAddress = ssr.getServiceAddress();
        this.serviceType = ssr.getServiceType();
        this.serviceTypeVersion = ssr.getServiceTypeVersion();
    }

    /**
     * @return Returns the serviceAddress.
     */
    public String getServiceAddress() {
        return serviceAddress;
    }

    /**
     * @param serviceAddress
     *            The serviceAddress to set.
     */
    public void setServiceAddress( String serviceAddress ) {
        this.serviceAddress = serviceAddress;
    }

    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     *            The serviceType to set.
     */
    public void setServiceType( String serviceType ) {
        this.serviceType = serviceType;
    }

    /**
     * @return Returns the serviceTypeVersion.
     */
    public String getServiceTypeVersion() {
        return serviceTypeVersion;
    }

    /**
     * @param serviceTypeVersion
     *            The serviceTypeVersion to set.
     */
    public void setServiceTypeVersion( String serviceTypeVersion ) {
        this.serviceTypeVersion = serviceTypeVersion;
    }

}
