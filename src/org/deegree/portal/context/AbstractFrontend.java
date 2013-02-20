//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/context/AbstractFrontend.java $ 
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

import java.util.ArrayList;
import java.util.List;

/**
 * this class encapsulates the description of the front end of a GUI setting up on a web map
 * context. this is a deegree specific form of description. beside some
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public abstract class AbstractFrontend implements Frontend {

    private GUIArea center = null;

    private GUIArea east = null;

    private GUIArea north = null;

    private GUIArea south = null;

    private GUIArea west = null;

    private String controller = null;

    /**
     * Creates a new Frontend object.
     * 
     * @param controller
     * @param west
     * @param east
     * @param south
     * @param north
     * @param center
     */
    public AbstractFrontend( String controller, GUIArea west, GUIArea east, GUIArea south, GUIArea north, GUIArea center ) {
        setController( controller );
        setWest( west );
        setEast( east );
        setSouth( south );
        setNorth( north );
        setCenter( center );
    }

    /**
     * @return the name of the central controller of the front end. depending on the implementation
     *         this may be the name of a HTML/JSP-page, a java class or something else.
     */
    public String getController() {
        return controller;
    }

    /**
     * @return the description of the west GUI area
     */
    public GUIArea getWest() {
        return west;
    }

    /**
     * @return the description of the east GUI area
     */
    public GUIArea getEast() {
        return east;
    }

    /**
     * @return the description of the south GUI area
     */
    public GUIArea getSouth() {
        return south;
    }

    /**
     * @return the description of the north GUI area
     */
    public GUIArea getNorth() {
        return north;
    }

    /**
     * @return the description of the central GUI area
     */
    public GUIArea getCenter() {
        return center;
    }

    /**
     * sets the name of the central controller of the front end. depending on the implementation
     * this may be the name of a HTML/JSP-page a java class or something else.
     * 
     * @param controller
     */
    public void setController( String controller ) {
        this.controller = controller;
    }

    /**
     * sets the description of the west GUI area
     * 
     * @param west
     */
    public void setWest( GUIArea west ) {
        this.west = west;
    }

    /**
     * sets the description of the east GUI area
     * 
     * @param east
     */
    public void setEast( GUIArea east ) {
        this.east = east;
    }

    /**
     * sets the description of the south GUI area
     * 
     * @param south
     */
    public void setSouth( GUIArea south ) {
        this.south = south;
    }

    /**
     * sets the description of the north GUI area
     * 
     * @param north
     */
    public void setNorth( GUIArea north ) {
        this.north = north;
    }

    /**
     * sets the description of the central GUI area
     * 
     * @param center
     */
    public void setCenter( GUIArea center ) {
        this.center = center;
    }

    /**
     * Returns the Modules of the given name (search order is north-east-south-west-center).
     * 
     * @param moduleName
     *            the name of the Module to extract from this Frontend.
     * @return an array of Modules of the given name. The array length may be 0, if no module of the
     *         given name is found.
     */
    public Module[] getModulesByName( String moduleName ) {

        List<Module> moduleList = new ArrayList<Module>( 5 );

        if ( getNorth() != null && getNorth().getModule( moduleName ) != null ) {
            moduleList.add( getNorth().getModule( moduleName ) );
        }
        if ( getEast() != null && getEast().getModule( moduleName ) != null ) {
            moduleList.add( getEast().getModule( moduleName ) );
        }
        if ( getSouth() != null && getSouth().getModule( moduleName ) != null ) {
            moduleList.add( getSouth().getModule( moduleName ) );
        }
        if ( getWest() != null && getWest().getModule( moduleName ) != null ) {
            moduleList.add( getWest().getModule( moduleName ) );
        }
        if ( getCenter() != null && getCenter().getModule( moduleName ) != null ) {
            moduleList.add( getCenter().getModule( moduleName ) );
        }
        return moduleList.toArray( new Module[moduleList.size()] );
    }

}