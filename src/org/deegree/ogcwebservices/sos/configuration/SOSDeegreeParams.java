//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/configuration/SOSDeegreeParams.java $
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

import org.deegree.enterprise.DeegreeParams;
import org.deegree.model.metadata.iso19115.OnlineResource;

/**
 * Describe the SCSDeegreeParams, is a part of the SCSConfiguration
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * 
 * @version 1.0
 */

public class SOSDeegreeParams extends DeegreeParams {

    private ArrayList<SensorConfiguration> sensorConfigs = null;

    private ArrayList<PlatformConfiguration> platformConfigs = null;

    private ArrayList<SourceServerConfiguration> sourceServerConfigs = null;

    private int sourceServerTimeLimit = 0;

    /**
     * constructor
     * 
     * @param defaultOnlineResource
     * @param cacheSize
     * @param requestTimeLimit
     * @param characterSet
     * @param sourceServerTimeLimit
     * @param sensorConfigs
     * @param platformConfigs
     * @param sourceServerConfigs
     */
    public SOSDeegreeParams( OnlineResource defaultOnlineResource, int cacheSize, int requestTimeLimit,
                             String characterSet, int sourceServerTimeLimit, SensorConfiguration[] sensorConfigs,
                             PlatformConfiguration[] platformConfigs, SourceServerConfiguration[] sourceServerConfigs ) {

        super( defaultOnlineResource, cacheSize, requestTimeLimit, characterSet );

        this.sourceServerTimeLimit = sourceServerTimeLimit;

        if ( sensorConfigs != null ) {
            this.sensorConfigs = new ArrayList<SensorConfiguration>( sensorConfigs.length );
            for ( int i = 0; i < sensorConfigs.length; i++ ) {
                this.sensorConfigs.add( sensorConfigs[i] );
            }
        } else {
            this.sensorConfigs = new ArrayList<SensorConfiguration>( 1 );
        }

        if ( platformConfigs != null ) {
            this.platformConfigs = new ArrayList<PlatformConfiguration>( platformConfigs.length );
            for ( int i = 0; i < platformConfigs.length; i++ ) {
                this.platformConfigs.add( platformConfigs[i] );
            }
        } else {
            this.platformConfigs = new ArrayList<PlatformConfiguration>( 1 );
        }

        if ( sourceServerConfigs != null ) {
            this.sourceServerConfigs = new ArrayList<SourceServerConfiguration>( sourceServerConfigs.length );
            for ( int i = 0; i < sourceServerConfigs.length; i++ ) {
                this.sourceServerConfigs.add( sourceServerConfigs[i] );
            }
        } else {
            this.sourceServerConfigs = new ArrayList<SourceServerConfiguration>( 1 );
        }

    }

    /**
     * gets the platform configuration by id
     * 
     * @param id
     * @return
     */
    public PlatformConfiguration getPlatformConfiguration( String id ) {
        for ( int i = 0; i < platformConfigs.size(); i++ ) {
            if ( platformConfigs.get( i ).getId().equals( id ) ) {
                return platformConfigs.get( i );
            }
        }
        return null;
    }

    /**
     * gets the Platform Configuration by the IdPropertyValue which is set in the configuration
     * 
     * @param id
     * @return
     */
    public PlatformConfiguration getPlatformConfigurationByIdPropertyValue( String id ) {
        for ( int i = 0; i < platformConfigs.size(); i++ ) {
            String s = platformConfigs.get( i ).getIdPropertyValue();
            if ( s.equals( id ) ) {
                return platformConfigs.get( i );
            }
        }

        return null;
    }

    /**
     * gets all platform configs
     * 
     * @return
     */
    public PlatformConfiguration[] getPlatformConfigurations() {
        return this.platformConfigs.toArray( new PlatformConfiguration[this.platformConfigs.size()] );
    }

    /**
     * gets the sensor configuration by id
     * 
     * @param id
     * @return
     */
    public SensorConfiguration getSensorConfiguration( String id ) {
        for ( int i = 0; i < sensorConfigs.size(); i++ ) {
            if ( sensorConfigs.get( i ).getId().equals( id ) ) {
                return sensorConfigs.get( i );
            }

        }

        return null;
    }

    /**
     * gets the Sensor Configuration by the IdPropertyValue which is set in the configuration
     * 
     * @param id
     * @return
     */
    public SensorConfiguration getSensorConfigurationByIdPropertyValue( String id ) {
        for ( int i = 0; i < this.sensorConfigs.size(); i++ ) {
            SensorConfiguration sc = this.sensorConfigs.get( i );
            String s = sc.getId();

            if ( s.equals( id ) ) {
                return this.sensorConfigs.get( i );
            }
        }

        return null;
    }

    /**
     * gets all sensor configs
     * 
     * @return
     */
    public SensorConfiguration[] getSensorConfigurations() {
        return this.sensorConfigs.toArray( new SensorConfiguration[this.sensorConfigs.size()] );
    }

    public int getSourceServerTimeLimit() {
        return sourceServerTimeLimit;
    }

    /**
     * gets all sourceServer Configurations
     * 
     * @return
     */
    public SourceServerConfiguration[] getSourceServerConfigurations() {
        return this.sourceServerConfigs.toArray( new SourceServerConfiguration[this.sourceServerConfigs.size()] );
    }

    /**
     * gets the sourceServer config by the given id
     * 
     * @param id
     * @return
     */
    public SourceServerConfiguration getSourceServerConfiguration( String id ) {
        for ( int i = 0; i < this.sourceServerConfigs.size(); i++ ) {
            if ( this.sourceServerConfigs.get( i ).getId().equals( id ) ) {
                return this.sourceServerConfigs.get( i );
            }
        }
        return null;
    }

}