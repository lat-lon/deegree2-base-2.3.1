//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcbase/Description.java $
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
package org.deegree.ogcbase;

import java.io.Serializable;

import org.deegree.ogcwebservices.MetadataLink;

/**
 * 
 * 
 * @version $Revision: 12071 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 12071 $, $Date: 2008-06-02 13:46:56 +0200 (Mo, 02. Jun 2008) $
 * 
 * @since 2.0
 */
public class Description extends DescriptionBase implements Cloneable, Serializable {

    private static final long serialVersionUID = -5160350013572694132L;

    private String label = null;

    /**
     * @param name
     * @param label
     * @throws OGCException
     */
    public Description( String name, String label ) throws OGCException {
        super( name );
        setLabel( label );
    }

    /**
     * @param description
     * @param name
     * @param label
     * @param metadataLink
     * @throws OGCException
     */
    public Description( String name, String label, String description, MetadataLink metadataLink ) throws OGCException {
        super( name, description, metadataLink );
        setLabel( label );
    }

    /**
     * @return Returns the label.
     * 
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            The label to set.
     * @throws OGCException
     * 
     */
    public void setLabel( String label )
                            throws OGCException {
        if ( label == null ) {
            throw new OGCException( "label must be <> null for Description" );
        }
        this.label = label;
    }

    @Override
    public Object clone() {
        try {
            MetadataLink metadataLink = getMetadataLink();
            if ( metadataLink != null ) {
                metadataLink = (MetadataLink) metadataLink.clone();
            }
            return new Description( getName(), label, getDescription(), metadataLink );

        } catch ( Exception e ) {
            // just return null
        }
        return null;
    }

}
