//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/describecoverage/AxisDescription.java $
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
package org.deegree.ogcwebservices.wcs.describecoverage;

import java.net.URI;

import org.deegree.datatypes.values.Values;
import org.deegree.ogcbase.Description;
import org.deegree.ogcbase.OGCException;
import org.deegree.ogcwebservices.MetadataLink;
import org.deegree.ogcwebservices.wcs.WCSException;

/**
 * @version $Revision: 9345 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */

public class AxisDescription extends Description implements Cloneable {

    private URI semantic = null;

    private URI refSys = null;

    private String refSysLabel = null;

    private Values values = null;

    /**
     * @param name
     * @param label
     * @param values
     */
    public AxisDescription( String name, String label, Values values ) throws OGCException, WCSException {
        super( name, label );
        setValues( values );
    }

    /**
     * @param name
     * @param label
     * @param description
     * @param metadataLink
     * @param semantic
     * @param refSys
     * @param refSysLabel
     * @param values
     */
    public AxisDescription( String name, String label, String description, MetadataLink metadataLink, URI semantic,
                            URI refSys, String refSysLabel, Values values ) throws OGCException, WCSException {
        super( name, label, description, metadataLink );
        this.semantic = semantic;
        this.refSys = refSys;
        this.refSysLabel = refSysLabel;
        setValues( values );
    }

    /**
     * @return Returns the refSys.
     */
    public URI getRefSys() {
        return refSys;
    }

    /**
     * @param refSys
     *            The refSys to set.
     */
    public void setRefSys( URI refSys ) {
        this.refSys = refSys;
    }

    /**
     * @return Returns the refSysLabel.
     */
    public String getRefSysLabel() {
        return refSysLabel;
    }

    /**
     * @param refSysLabel
     *            The refSysLabel to set.
     */
    public void setRefSysLabel( String refSysLabel ) {
        this.refSysLabel = refSysLabel;
    }

    /**
     * @return Returns the semantic.
     */
    public URI getSemantic() {
        return semantic;
    }

    /**
     * @param semantic
     *            The semantic to set.
     */
    public void setSemantic( URI semantic ) {
        this.semantic = semantic;
    }

    /**
     * @return Returns the values.
     */
    public Values getValues() {
        return values;
    }

    /**
     * @param values
     *            The values to set.
     */
    public void setValues( Values values )
                            throws WCSException {
        if ( values == null ) {
            throw new WCSException( "values must be <> null for AxisDescription" );
        }
        this.values = values;
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        Values values_ = null;
        if ( values != null ) {
            values_ = (Values) values.clone();
        }
        try {
            Description des = (Description) super.clone();
            return new AxisDescription( des.getName(), des.getLabel(), des.getDescription(), des.getMetadataLink(),
                                        semantic, refSys, refSysLabel, values_ );
        } catch ( Exception e ) {
        }
        return null;
    }

}
