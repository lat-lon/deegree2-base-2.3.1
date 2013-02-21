//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/filterencoding/FalseFilter.java $
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
 Aennchenstr. 19
 53115 Bonn
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
package org.deegree.model.filterencoding;

import org.deegree.model.feature.Feature;

/**
 * A {@link Filter} that always evaluates to false.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 13773 $, $Date: 2008-08-28 16:51:49 +0200 (Do, 28. Aug 2008) $
 */
public class FalseFilter implements Filter {

    /**
     * Calculates the <tt>Filter</tt>'s logical value (false).
     * <p>
     * 
     * @param feature
     *            (in this special case irrelevant)
     * @return false (always)
     */
    public boolean evaluate( Feature feature ) {
        return false;
    }

    public StringBuffer toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append( "<ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'>" );
        sb.append( "<False/>" );
        sb.append( "</ogc:Filter>\n" );
        return sb;
    }
    
    public StringBuffer to100XML() {
        return toXML();
    }

    public StringBuffer to110XML() {
        return toXML();        
    }    
}
