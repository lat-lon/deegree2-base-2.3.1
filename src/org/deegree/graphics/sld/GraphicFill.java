//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/sld/GraphicFill.java $
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
 * The GraphicFill element both indicates that a stipple-fill repeated graphic will be used and
 * specifies the fill graphic.
 * <p>
 * </p>
 * A graphic can be defined very informally as a little picture. The appearance of the graphic is
 * defined with the embedded Graphic element. Additional parameters for the GraphicFill may be
 * provided in the future to provide more control the exact style of filling.
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 10547 $, $Date: 2008-03-11 09:40:28 +0100 (Tue, 11 Mar 2008) $
 */
public class GraphicFill implements Marshallable {

    /**
     * 
     */
    private Graphic graphic = null;

    /**
     * default constructor
     */
    GraphicFill() {
    }

    /**
     * constructor initializing the class with the <GraphicFill>
     */
    GraphicFill( Graphic graphic ) {
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
     * sets <Graphic>
     * 
     * @param graphic
     * 
     */
    public void setGraphic( Graphic graphic ) {
        this.graphic = graphic;
    }

    /**
     * exports the content of the GraphicFill as XML formated String
     * 
     * @return xml representation of the GraphicFill
     */
    public String exportAsXML() {

        StringBuffer sb = new StringBuffer( 1000 );
        sb.append( "<GraphicFill>" );
        sb.append( ( (Marshallable) graphic ).exportAsXML() );
        sb.append( "</GraphicFill>" );

        return sb.toString();
    }

}
