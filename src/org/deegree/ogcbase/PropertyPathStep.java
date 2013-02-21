//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcbase/PropertyPathStep.java $
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
package org.deegree.ogcbase;

import org.deegree.datatypes.QualifiedName;

/**
 * Represents one step of a {@link PropertyPath}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 14662 $, $Date: 2008-11-03 17:45:46 +0100 (Mo, 03. Nov 2008) $
 * 
 * @see PropertyPath
 */
public abstract class PropertyPathStep {

    protected QualifiedName propertyName;

    /**
     * Creates a new instance of <code>PropertyPathStep</code> that selects the property with the
     * given name.
     * 
     * @param propertyName
     */
    PropertyPathStep( QualifiedName propertyName ) {
        this.propertyName = propertyName;
    }

    /**
     * Returns the name of the selected property.
     * 
     * @return the name of the property
     */
    public QualifiedName getPropertyName() {
        return this.propertyName;
    }

    @Override
    public int hashCode() {
        return this.propertyName.hashCode();
    }

    public void setPropertyName( QualifiedName propertyName ) {
       this.propertyName = propertyName;
    }
}
