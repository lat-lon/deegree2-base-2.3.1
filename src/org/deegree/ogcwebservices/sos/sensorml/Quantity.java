//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/sensorml/Quantity.java $
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

import java.net.URI;

/**
 * represents a Quantity; please read the SensorML spec
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 *  
 */

public class Quantity {

	private double value = Double.NaN;

	private boolean fixed = true;

	private URI uom = null;

	private double min = Double.NaN;

	private double max = Double.NaN;

    /**
     *
     * @param value
     */
	public Quantity(double value) {
		this.value = value;
	}

    /**
     * 
     * @return fixed
     */
	public boolean isFixed() {
		return fixed;
	}

    /**
     *
     * @return max
     */
	public double getMax() {
		return max;
	}

    /**
     * 
     * @return min
     */
	public double getMin() {
		return min;
	}

    /**
     * 
     * @return uom
     */
	public URI getUom() {
		return uom;
	}

    /**
     * 
     * @return value
     */
	public double getValue() {
		return value;
	}

    /**
     * 
     * @param fixed
     */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

    /**
     * 
     * @param max
     */
	public void setMax(double max) {
		this.max = max;
	}

    /**
     * 
     * @param min
     */
	public void setMin(double min) {
		this.min = min;
	}

    /**
     * 
     * @param uom
     */
	public void setUom(URI uom) {
		this.uom = uom;
	}
}