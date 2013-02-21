//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/sensorml/LocationModel.java $
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

/**
 * represents a LocationModel; please read the SensorML spec
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 *  
 */

public abstract class LocationModel {

	private String id = null;

	private Identifier[] identifiedAs = null;

	private Classifier[] classifiedAs = null;

	private Discussion[] description = null;

	private EngineeringCRS sourceCRS = null;

	private CoordinateReferenceSystem referenceCRS = null;

    /**
     * 
     * @param id
     * @param identifiedAs
     * @param classifiedAs
     * @param description
     * @param sourceCRS
     * @param referenceCRS
     */
	protected LocationModel(String id, Identifier[] identifiedAs,
			Classifier[] classifiedAs, Discussion[] description,
			EngineeringCRS sourceCRS, CoordinateReferenceSystem referenceCRS) {

		this.id = id;
		this.identifiedAs = identifiedAs;
		this.classifiedAs = classifiedAs;
		this.description = description;
		this.sourceCRS = sourceCRS;
		this.referenceCRS = referenceCRS;

	}

    /**
     * 
     * @return classifiers
     */
	public Classifier[] getClassifiedAs() {
		return classifiedAs;
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
     * @return identfiers
     */
	public Identifier[] getIdentifiedAs() {
		return identifiedAs;
	}

    /**
     * 
     * @return reference CRS
     */
	public CoordinateReferenceSystem getReferenceCRS() {
		return referenceCRS;
	}

    /**
     * 
     * @return sourceCRS
     */
	public EngineeringCRS getSourceCRS() {
		return sourceCRS;
	}

    /**
     * 
     * @return identifier
     */
	public String getId() {
		return id;
	}
}