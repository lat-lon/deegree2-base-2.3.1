//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcbase/DescriptionBase.java $
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

import org.deegree.ogcwebservices.MetadataLink;

/**
 * @version $Revision: 12071 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 12071 $, $Date: 2008-06-02 13:46:56 +0200 (Mo, 02. Jun 2008) $
 * 
 * @since 2.0
 */

public class DescriptionBase implements Cloneable {

    private String name = null;

    private String description = null;

    private MetadataLink metadataLink = null;

    /**
     * just <tt>name</tt> is mandatory
     * 
     * @param name
     * @throws OGCException
     */
    public DescriptionBase( String name ) throws OGCException {
        setName( name );
    }

    /**
     * @param description
     * @param name
     * @param metadataLink
     * @throws OGCException
     */
    public DescriptionBase( String name, String description, MetadataLink metadataLink ) throws OGCException {
        this.description = description;
        setName( name );
        this.metadataLink = metadataLink;
    }

    /**
     * @return Returns the description.
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     * 
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * @return Returns the metadataLink.
     * 
     */
    public MetadataLink getMetadataLink() {
        return metadataLink;
    }

    /**
     * @param metadataLink
     *            The metadataLink to set.
     * 
     */
    public void setMetadataLink( MetadataLink metadataLink ) {
        this.metadataLink = metadataLink;
    }

    /**
     * @return Returns the name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     * @throws OGCException
     * 
     */
    public void setName( String name )
                            throws OGCException {
        if ( name == null ) {
            throw new OGCException( "name must be <> null for DescriptionBase" );
        }
        this.name = name;
    }

    @Override
    public Object clone() {
        try {
            MetadataLink ml = null;
            if ( metadataLink != null ) {
                ml = (MetadataLink) metadataLink.clone();
            }
            return new DescriptionBase( name, description, ml );
        } catch ( Exception e ) {
            // just return null
        }
        return null;
    }

}