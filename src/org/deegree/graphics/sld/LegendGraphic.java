//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/sld/LegendGraphic.java $
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
package org.deegree.graphics.sld;

import org.deegree.framework.xml.Marshallable;

/**
 * The LegendGraphic element gives an optional explicit Graphic symbol to be displayed in a legend
 * for this rule.
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 12143 $, $Date: 2008-06-04 11:25:13 +0200 (Mi, 04. Jun 2008) $
 */
public class LegendGraphic implements Marshallable {

    /**
     * 
     */
    private Graphic graphic = null;

    /**
     * default constructor
     */
    LegendGraphic() {
        //nothing
    }

    /**
     * constructor initializing the class with the <LegendGraphic>
     * @param graphic 
     */
    LegendGraphic( Graphic graphic ) {
        setGraphic( graphic );
    }

    /**
     * A Graphic is a graphic symbol with an inherent shape, color(s), and possibly size. A graphic
     * can be very informally defined as a little picture and can be of either a raster or
     * vector-graphic source type. The term graphic is used since the term symbol is similar to
     * symbolizer which is used in a different context in SLD.
     * 
     * @return graphic
     * 
     */
    public Graphic getGraphic() {
        return graphic;
    }

    /**
     * sets the <Graphic>
     * 
     * @param graphic
     * 
     */
    public void setGraphic( Graphic graphic ) {
        this.graphic = graphic;
    }

    /**
     * exports the content of the Font as XML formated String
     * 
     * @return xml representation of the Font
     */
    public String exportAsXML() {

        StringBuffer sb = new StringBuffer( 1000 );
        sb.append( "<LegendGraphic>" );
        sb.append( ( (Marshallable) graphic ).exportAsXML() );
        sb.append( "</LegendGraphic>" );

        return sb.toString();
    }
}