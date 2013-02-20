//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/xml/schema/ElementReference.java $
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
package org.deegree.framework.xml.schema;

import org.deegree.datatypes.QualifiedName;

/**
 * Represents an element reference. The reference may be resolved or not. If it is resolved, the
 * referenced <code>ElementDeclaration</code> is accessible, otherwise only the name of the
 * element is available.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9339 $, $Date: 2007-12-27 13:31:52 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class ElementReference {

    private QualifiedName elementName;

    private ElementDeclaration declaration;

    private boolean isResolved;

    /**
     * Creates an unresolved <code>ElementReference</code>.
     * 
     * @param elementName
     */
    public ElementReference( QualifiedName elementName ) {
        this.elementName = elementName;
    }

    public QualifiedName getName() {
        return this.elementName;
    }

    public boolean isResolved() {
        return this.isResolved;
    }

    public ElementDeclaration getElementDeclaration() {
        return this.declaration;
    }

    public void resolve( ElementDeclaration declaration ) {
        if ( isResolved() ) {
            throw new RuntimeException( "ElementDeclaration to element '"
                + elementName + "' has already been resolved." );
        }
        this.declaration = declaration;
        this.isResolved = true;
    }

    public void resolve() {
        if ( isResolved() ) {
            throw new RuntimeException( "ElementDeclaration to element '"
                + elementName + "' has already been resolved." );
        }
        this.isResolved = true;
    }    
}
