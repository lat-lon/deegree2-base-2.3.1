//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/capabilities/Identifier.java $
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
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de
 
 ---------------------------------------------------------------------------*/

package org.deegree.ogcwebservices.wpvs.capabilities;

import java.net.URI;

/**
 * TODO there are other Identifier classes around!
 * ogcwebservices.sos.sensorml.Identifier and the interface org.opengis.metadata.Identifier
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class Identifier {
	
	private String value;
	
	private URI codeSpace;

	/**
	 * Creates a new identifier object from value and codeSpace.
	 * 
	 * @param value
	 * @param codeSpace maybe null
	 */
	public Identifier(String value, URI codeSpace){
		this.value = value;
		this.codeSpace = codeSpace;
	}

	/**
	 * @return Returns the codeSpace.
	 */
	public URI getCodeSpace() {
		return codeSpace;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
    
    /**
     * @param other identifier instance
     * @return true if value and code space are the same.
     */
    public boolean equals( Identifier other ){
        return (value.equals(other.value) && codeSpace.equals(other.getCodeSpace() ));
    }
    
    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer( "Codespace: " );
        sb.append( ((codeSpace!=null)? codeSpace.toASCIIString() : "no codespace defined") );
        sb.append(", value: " ).append(value);
        return sb.toString();
    }
	
}
