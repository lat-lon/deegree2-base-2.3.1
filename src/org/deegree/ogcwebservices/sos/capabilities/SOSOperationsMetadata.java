//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/capabilities/SOSOperationsMetadata.java $
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
package org.deegree.ogcwebservices.sos.capabilities;

import org.deegree.ogcwebservices.getcapabilities.Operation;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;

/**
 * Describe the OWS OperationsMetadata, is a part of the SCS Capabilities
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe</a>
 * 
 * @version 1.0
 */
public class SOSOperationsMetadata extends OperationsMetadata {

    public static final String DESCRIBE_PLATFORM_NAME = "DescribePlatform";

    public static final String DESCRIBE_SENSOR_NAME = "DescribeSensor";

    public static final String GET_OBSERVATION_NAME = "GetObservation";

    private Operation describePlatform;

    private Operation describeSensor;

    private Operation getObservation;

    /**
     * constructor
     * 
     * @param getCapabilities
     * @param describePlatform
     * @param describeSensor
     * @param getObservation
     */
    public SOSOperationsMetadata( Operation getCapabilities, Operation describePlatform,
                                  Operation describeSensor, Operation getObservation ) {

        super( getCapabilities, null, null );

        this.describePlatform = describePlatform;
        this.describeSensor = describeSensor;
        this.getObservation = getObservation;

    }

    /**
     * 
     */
    public Operation[] getOperations() {
        return new Operation[] { getCapabilitiesOperation, describePlatform, describeSensor,
                                getObservation };
    }

    /**
     * 
     * @return describePlatform
     */
    public Operation getDescribePlatform() {
        return describePlatform;
    }

    /**
     * 
     * @param describePlatform
     */
    public void setDescribePlatform( Operation describePlatform ) {
        this.describePlatform = describePlatform;
    }

    /**
     * 
     * @return describeSensor
     */
    public Operation getDescribeSensor() {
        return describeSensor;
    }

    /**
     * 
     * @param describeSensor
     */
    public void setDescribeSensor( Operation describeSensor ) {
        this.describeSensor = describeSensor;
    }

    /**
     * 
     * @return getObservation
     */
    public Operation getGetObservation() {
        return getObservation;
    }

    /**
     * 
     * @param getObservation
     */
    public void setGetObservation( Operation getObservation ) {
        this.getObservation = getObservation;
    }
}
