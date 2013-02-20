//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/configuration/SensorConfiguration.java $
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
package org.deegree.ogcwebservices.sos.configuration;

import java.util.ArrayList;

/**
 * represents a sensor configuration
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * @author last edited by: $Author:wanhoff$
 * 
 * @version $Revision: 9345 $, $Date:20.03.2007$
 */
public class SensorConfiguration {

    private String id = null;

    private String idPropertyValue = null;

    private String sourceServerId = null;

    private ArrayList<MeasurementConfiguration> measurementConfigurations = new ArrayList<MeasurementConfiguration>();

    /**
     * constructor
     * 
     * @param id
     * @param idPropertyValue
     * @param sourceServerId
     * @param measurements
     */
    public SensorConfiguration( String id, String idPropertyValue, String sourceServerId,
                                MeasurementConfiguration[] measurements ) {
        this.id = id;
        this.idPropertyValue = idPropertyValue;
        this.sourceServerId = sourceServerId;

        if ( measurements != null ) {
            for ( int i = 0; i < measurements.length; i++ ) {
                this.measurementConfigurations.add( measurements[i] );
            }
        } else {

        }
    }

    /**
     * returns all measurements
     * 
     * @return all measurements
     */
    public MeasurementConfiguration[] getMeasurementConfigurations() {
        return measurementConfigurations.toArray( new MeasurementConfiguration[measurementConfigurations.size()] );
    }

    /**
     * returns the first measurement; is the default if the user don't request a special measurement
     * 
     * @return the first measurement
     */
    public MeasurementConfiguration getFirstMeasurementConfiguration() {
        return this.measurementConfigurations.get( 0 );
    }

    /**
     * 
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @return idPropertyValue
     */
    public String getIdPropertyValue() {
        return idPropertyValue;
    }

    /**
     * 
     * @return sourceServerId
     */
    public String getSourceServerId() {
        return sourceServerId;
    }

    /**
     * overwrites the equals function
     * 
     * @param obj
     * @return
     */
    public boolean equals( Object obj ) {
        if ( !( obj instanceof SensorConfiguration ) ) {
            return false;
        }
        if ( this.getId().equals( ( (SensorConfiguration) obj ).getId() ) ) {
            return true;
        }
        return false;
    }

    /**
     * returns the Measurement Config by the given id
     * 
     * @param id
     * @return the Measurement Config by the given id
     */
    public MeasurementConfiguration getMeasurementById( String id ) {
        for ( int i = 0; i < this.measurementConfigurations.size(); i++ ) {
            if ( this.measurementConfigurations.get( i ).equals( id ) ) {
                return this.measurementConfigurations.get( i );
            }
        }
        return null;
    }

}