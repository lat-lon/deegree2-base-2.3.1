//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/execute/ComplexValue.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
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
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de
 
 ---------------------------------------------------------------------------*/
package org.deegree.ogcwebservices.wps.execute;

import java.net.URI;
import java.net.URL;

/**
 * ComplexValue.java
 * 
 * Created on 24.03.2006. 16:35:15h
 * 
 * Identifies this input or output value as a complex value data structure
 * encoded in XML (e.g., using GML), and provides that complex value data
 * structure. For an input, this element may be used by a client for any process
 * input coded as ComplexData in the ProcessDescription. For an output, this
 * element shall be used by a server when "store" in the Execute request is
 * "false".
 * 
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * 
 * @version 1.0.
 * 
 * @since 2.0
 */

public class ComplexValue extends ComplexValueEncoding {

	protected Object value;

	/**
	 * @param encoding
	 * @param format
	 * @param schema
	 * @param value to set
	 */
	public ComplexValue( String format, URI encoding, URL schema, Object value ) {
		super( format, encoding, schema );
		this.value = value;

	}

	/**
	 * @return Returns the content.
	 */
	public Object getContent() {
		return value;
	}

}
