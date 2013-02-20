//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/context/ViewContextCollection.java $
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

import java.util.HashMap;

/**
 * This class encapsulates a collection of references to Web Map Context documents as defined by the
 * OGC Web Map Context specification
 * 
 * @version $Revision: 9346 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 */
public class ViewContextCollection {
    private HashMap<String, ViewContextReference> viewContextReferences = new HashMap<String, ViewContextReference>();

    /**
     * Creates a new ViewContextCollection object.
     * 
     * @param viewContextReferences
     * 
     * @throws ContextException
     */
    public ViewContextCollection( ViewContextReference[] viewContextReferences ) throws ContextException {
        setViewContextReferences( viewContextReferences );
    }

    /**
     * 
     * 
     * @return all ViewContextReference
     */
    public ViewContextReference[] getViewContextReferences() {
        ViewContextReference[] fr = new ViewContextReference[viewContextReferences.size()];
        return viewContextReferences.values().toArray( fr );
    }

    /**
     * 
     * 
     * @param viewContextReferences
     * 
     * @throws ContextException
     */
    public void setViewContextReferences( ViewContextReference[] viewContextReferences )
                            throws ContextException {
        if ( ( viewContextReferences == null ) || ( viewContextReferences.length == 0 ) ) {
            throw new ContextException( "at least one viewContextReference must be defined for a layer" );
        }

        this.viewContextReferences.clear();

        for ( int i = 0; i < viewContextReferences.length; i++ ) {
            this.viewContextReferences.put( viewContextReferences[i].getTitle(), viewContextReferences[i] );
        }
    }

    /**
     * 
     * 
     * @param name
     * 
     * @return named ViewContextReference
     */
    public ViewContextReference getViewContextReference( String name ) {
        return viewContextReferences.get( name );
    }

    /**
     * 
     * 
     * @param viewContextReference
     */
    public void addViewContextReference( ViewContextReference viewContextReference ) {
        viewContextReferences.put( viewContextReference.getTitle(), viewContextReference );
    }

    /**
     * 
     * 
     * @param name
     * 
     * @return removed ViewContextReference
     */
    public ViewContextReference removeViewContextReference( String name ) {
        return viewContextReferences.remove( name );
    }

    /**
     * 
     */
    public void clear() {
        viewContextReferences.clear();
    }

}