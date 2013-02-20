//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/context/Frontend.java $
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
package org.deegree.portal.context;

/**
 * this interface the description the access to the front end elements of a GUI setting up on a web map context. this is
 * a deegree specific form of description. beside the name of the central controlling element an implementing class
 * enables access to fife areas the GUI is splitted in.
 * 
 * @version $Revision: 10963 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 */
public interface Frontend {

    /**
     * @return the name of the central controller of the front end. depending on the implementation this may be the name
     *         of a HTML/JSP-page a java class or something else.
     */
    public String getController();

    /**
     * @return the description of the west GUI area
     */
    public GUIArea getWest();

    /**
     * @return the description of the east GUI area
     */
    public GUIArea getEast();

    /**
     * @return the description of the south GUI area
     */
    public GUIArea getSouth();

    /**
     * @return the description of the north GUI area
     */
    public GUIArea getNorth();

    /**
     * @return the description of the central GUI area
     * 
     */
    public GUIArea getCenter();

    /**
     * sets the name of the central controller of the front end. depending on the implementation this may be the name of
     * a HTML/JSP-page a java class or something else.
     * 
     * @param controller
     */
    public void setController( String controller );

    /**
     * sets the description of the west GUI area
     * 
     * @param west
     */
    public void setWest( GUIArea west );

    /**
     * sets the description of the east GUI area
     * 
     * @param east
     */
    public void setEast( GUIArea east );

    /**
     * sets the description of the south GUI area
     * 
     * @param south
     */
    public void setSouth( GUIArea south );

    /**
     * sets the description of the north GUI area
     * 
     * @param north
     */
    public void setNorth( GUIArea north );

    /**
     * sets the description of the central GUI area
     * 
     * @param center
     */
    public void setCenter( GUIArea center );
}