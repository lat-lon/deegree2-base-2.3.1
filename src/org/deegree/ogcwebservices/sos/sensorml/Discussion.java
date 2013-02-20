//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/sensorml/Discussion.java $
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
 * represents a Discussion; please read the SensorML spec
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 *  
 */

public class Discussion {

	private String value = null;

	private URI topic = null;

	private URI codeSpace = null;

	private String id = null;

    /**
     * 
     * @param value
     * @param topic
     * @param codeSpace
     * @param id
     */
	public Discussion(String value, URI topic, URI codeSpace, String id) {
		this.value = value;
		this.topic = topic;
		this.codeSpace = codeSpace;
		this.id = id;
	}

    /**
     * 
     * @return codespace
     */
	public URI getCodeSpace() {
		return codeSpace;
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
     * @return topic
     */
	public URI getTopic() {
		return topic;
	}

    /**
     * 
     * @return value
     */
	public String getValue() {
		return value;
	}

}