//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/sensorml/ResponseModel.java $
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
 * represents a ResponseModel; please read the SensorML spec
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 10547 $, $Date: 2008-03-11 09:40:28 +0100 (Tue, 11 Mar 2008) $
 */
public class ResponseModel {

    private Identifier[] identifiedAs = null;

    private Classifier[] classifiedAs = null;

    private Discussion[] description = null;

    private Object[] usesParameters = null;

    private String id = null;

    /**
     * 
     * @param id
     * @param identifiedAs
     * @param classifiedAs
     * @param description
     * @param usesParametersObjects
     */
    public ResponseModel( String id, Identifier[] identifiedAs, Classifier[] classifiedAs, Discussion[] description,
                          Object[] usesParametersObjects ) {
        this.id = id;
        this.identifiedAs = identifiedAs;
        this.classifiedAs = classifiedAs;
        this.description = description;

        // use only valid objects
        ArrayList<Object> temp = new ArrayList<Object>();
        for ( int i = 0; i < usesParametersObjects.length; i++ ) {
            if ( usesParametersObjects[i] instanceof BasicResponse ) {
                temp.add( usesParametersObjects[i] );
            }

            /*
             * other possible types are ImpulseResponse but not yet implemented
             */
        }

        this.usesParameters = temp.toArray( new Object[temp.size()] );
    }

    /**
     * 
     * @return classifiers
     */
    public Classifier[] getClassifiedAs() {
        return classifiedAs;
    }

    /**
     * descriptions
     * @return
     */
    public Discussion[] getDescription() {
        return description;
    }

    /**
     * 
     * @return identifiers
     */
    public Identifier[] getIdentifiedAs() {
        return identifiedAs;
    }

    /**
     * 
     * @return uses parameters
     */
    public Object[] getUsesParameters() {
        return usesParameters;
    }

    /**
     * 
     * @return id
     */
    public String getId() {
        return id;
    }
}