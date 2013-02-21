//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/sensorml/Product.java $
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
 * represents a Product; please read the SensorML spec
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 *  
 */

public class Product {

	private Identifier[] identifiedAs = null;

	private Classifier[] classifiedAs = null;

	private Discussion[] description = null;

	private LocationModel[] locatedUsing = null;

	private EngineeringCRS hasCRS = null;

	private Phenomenon observable = null;

	private String id = null;

	private ResponseModel[] derivedFrom = null;

    /**
     * 
     * @param identifiedAs
     * @param classifiedAs
     * @param description
     * @param locatedUsing
     * @param derivedFrom
     * @param hasCRS
     * @param observable
     * @param id
     */
	public Product(Identifier[] identifiedAs, Classifier[] classifiedAs,
			Discussion[] description, LocationModel[] locatedUsing,
			ResponseModel[] derivedFrom, EngineeringCRS hasCRS,
			Phenomenon observable, String id) {

		this.identifiedAs = identifiedAs;
		this.classifiedAs = classifiedAs;
		this.description = description;
		this.locatedUsing = locatedUsing;
		this.derivedFrom = derivedFrom;
		this.hasCRS = hasCRS;
		this.observable = observable;
		this.id = id;
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
     * @return CRS
     */
	public EngineeringCRS getHasCRS() {
		return hasCRS;
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
     * @return located using
     */
	public LocationModel[] getLocatedUsing() {
		return locatedUsing;
	}

    /**
     * 
     * @return observable
     */
	public Phenomenon getObservable() {
		return observable;
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
     * @return derived from
     */
	public ResponseModel[] getDerivedFrom() {
		return derivedFrom;
	}
}
