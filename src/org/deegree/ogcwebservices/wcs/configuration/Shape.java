// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/configuration/Shape.java $
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
package org.deegree.ogcwebservices.wcs.configuration;

import org.deegree.model.crs.CoordinateSystem;

/**
 * 
 * 
 * @version $Revision: 10679 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10679 $, $Date: 2008-03-25 18:26:57 +0100 (Di, 25. Mär 2008) $
 * 
 * @since 2.0
 */
public class Shape extends CoverageSource {

    private String rootFileName = null;

    private String tileProperty = null;

    private String directoryProperty = null;

    /**
     * @param crs
     * @param rootFileName
     * @param tileProperty
     * @param directoryProperty
     */
    public Shape( CoordinateSystem crs, String rootFileName, String tileProperty, String directoryProperty ) {
        super( crs );
        this.rootFileName = rootFileName;
        this.tileProperty = tileProperty;
        this.directoryProperty = directoryProperty;
    }

    /**
     * @return Returns the directoryProperty.
     */
    public String getDirectoryProperty() {
        return directoryProperty;
    }

    /**
     * @param directoryProperty
     *            The directoryProperty to set.
     */
    public void setDirectoryProperty( String directoryProperty ) {
        this.directoryProperty = directoryProperty;
    }

    /**
     * @return Returns the rootFileName.
     */
    public String getRootFileName() {
        return rootFileName;
    }

    /**
     * @param rootFileName
     *            The rootFileName to set.
     */
    public void setRootFileName( String rootFileName ) {
        this.rootFileName = rootFileName;
    }

    /**
     * @return Returns the tileProperty.
     */
    public String getTileProperty() {
        return tileProperty;
    }

    /**
     * @param tileProperty
     *            The tileProperty to set.
     */
    public void setTileProperty( String tileProperty ) {
        this.tileProperty = tileProperty;
    }

}
