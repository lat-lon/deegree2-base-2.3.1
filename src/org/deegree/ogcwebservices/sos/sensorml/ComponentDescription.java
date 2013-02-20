//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/sensorml/ComponentDescription.java $
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
 Aennchenstraße 19  
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 lat/lon GmbH
 Aennchenstraße 19
 53177 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.ogcwebservices.sos.sensorml;

import org.deegree.model.metadata.iso19115.CitedResponsibleParty;

/**
 * represents a ComponentDescription; please read the SensorML spec
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * 
 */

public class ComponentDescription {

    private String id = null;

    private CitedResponsibleParty[] manufacturedBy = null;

    private CitedResponsibleParty[] deployedBy = null;

    private CitedResponsibleParty[] operatedBy = null;

    private Discussion[] description = null;

    private Reference[] reference = null;

    /**
     * 
     * @param id
     * @param manufacturedBy
     * @param deployedBy
     * @param operatedBy
     * @param description
     * @param reference
     */
    public ComponentDescription( String id, CitedResponsibleParty[] manufacturedBy, CitedResponsibleParty[] deployedBy,
                                 CitedResponsibleParty[] operatedBy, Discussion[] description, Reference[] reference ) {

        this.id = id;
        this.manufacturedBy = manufacturedBy;
        this.deployedBy = deployedBy;
        this.operatedBy = operatedBy;
        this.description = description;
        this.reference = reference;

    }

    /**
     * 
     * @return deployed by
     */
    public CitedResponsibleParty[] getDeployedBy() {
        return deployedBy;
    }

    /**
     * 
     * @return descriptions
     */
    public Discussion[] getDescription() {
        return description;
    }

    /**
     * 
     * @return identifier
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @return manufactured by
     */
    public CitedResponsibleParty[] getManufacturedBy() {
        return manufacturedBy;
    }

    /**
     * 
     * @return operated by
     */
    public CitedResponsibleParty[] getOperatedBy() {
        return operatedBy;
    }

    /**
     * 
     * @return references
     */
    public Reference[] getReference() {
        return reference;
    }

}