//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/sensorml/GeoPositionModel.java $
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

import java.util.ArrayList;

/**
 * represents a GeoPositionModel; please read the SensorML spec
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * @author last edited by: $Author:wanhoff$
 * 
 * @version $Revision: 9345 $, $Date:20.03.2007$
 */
public class GeoPositionModel extends LocationModel {

    private Object[] usesParametersObjects = null;

    /**
     * @param id
     * @param identifiedAs
     * @param classifiedAs
     * @param description
     * @param sourceCRS
     * @param referenceCRS
     * @param usesParametersObjects
     *            cannot be null
     */
    public GeoPositionModel( String id, Identifier[] identifiedAs, Classifier[] classifiedAs,
                             Discussion[] description, EngineeringCRS sourceCRS,
                             CoordinateReferenceSystem referenceCRS, Object[] usesParametersObjects ) {

        super( id, identifiedAs, classifiedAs, description, sourceCRS, referenceCRS );

        // use only valid objects
        ArrayList<Object> temp = new ArrayList<Object>();
        for ( int i = 0; i < usesParametersObjects.length; i++ ) {
            if ( usesParametersObjects[i] instanceof GeoLocation ) {
                temp.add( usesParametersObjects[i] );
            }

            /*
             * other possible types are Attitude, GeoPosition and Position but not yet implemented
             */
        }

        this.usesParametersObjects = temp.toArray( new Object[temp.size()] );

    }

    /**
     * @return usesParametersObjects
     */
    public Object[] getUsesParametersObjects() {
        return usesParametersObjects;
    }

}