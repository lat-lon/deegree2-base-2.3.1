//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/ComponentMetadata.java $
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
package org.deegree.ogcwebservices.sos;

import org.deegree.ogcwebservices.sos.sensorml.Classifier;
import org.deegree.ogcwebservices.sos.sensorml.ComponentDescription;
import org.deegree.ogcwebservices.sos.sensorml.EngineeringCRS;
import org.deegree.ogcwebservices.sos.sensorml.Identifier;
import org.deegree.ogcwebservices.sos.sensorml.LocationModel;

/**
 * holds the metadata of a generic component; the necessary parts of the SensorML spec are
 * implemented; please read the SensorML spec for more info
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 */
public abstract class ComponentMetadata {

    private Identifier[] identifiedAs = null;

    private Classifier[] classifiedAs = null;

    private EngineeringCRS hasCRS = null;

    private LocationModel[] locatedUsing = null;

    private ComponentDescription describedBy = null;

    private String attachedTo = null;

    /**
     * Constructor
     * 
     * @param identifiedAs
     * @param classifiedAs
     * @param hasCRS
     * @param locatedUsing
     * @param describedBy
     * @param attachedTo
     */
    protected ComponentMetadata( Identifier[] identifiedAs, Classifier[] classifiedAs, EngineeringCRS hasCRS,
                                 LocationModel[] locatedUsing, ComponentDescription describedBy, String attachedTo ) {

        this.identifiedAs = identifiedAs;
        this.classifiedAs = classifiedAs;
        this.hasCRS = hasCRS;
        this.locatedUsing = locatedUsing;
        this.describedBy = describedBy;
        this.attachedTo = attachedTo;
    }

    /**
     * 
     * @return attachedTo
     */
    public String getAttachedTo() {
        return attachedTo;
    }

    /**
     * 
     * @return classifiedAs
     */
    public Classifier[] getClassifiedAs() {
        return classifiedAs;
    }

    /**
     * 
     * @return describedBy
     */
    public ComponentDescription getDescribedBy() {
        return describedBy;
    }

    /**
     * 
     * @return hasCRS
     */
    public EngineeringCRS getHasCRS() {
        return hasCRS;
    }

    /**
     * 
     * @return identifiedAs
     */
    public Identifier[] getIdentifiedAs() {
        return identifiedAs;
    }

    /**
     * 
     * @return locatedUsing
     */
    public LocationModel[] getLocatedUsing() {
        return locatedUsing;
    }

}
