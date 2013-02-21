// $HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/ParameterValueGroupIm.java $
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
package org.deegree.datatypes.parameter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A group of related parameter values. The same group can be repeated more than once in an or
 * higher level <code>ParameterValueGroup</code>, if those instances contain different values of
 * one or more which suitably distinquish among those groups.
 * 
 * @version $Revision: 11711 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 11711 $, $Date: 2008-05-14 11:15:46 +0200 (Mi, 14. Mai 2008) $
 * 
 * @since 2.0
 * @deprecated Not required. Will be deleted.
 */
@Deprecated
public class ParameterValueGroupIm extends GeneralParameterValueIm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, GeneralParameterValueIm> values = new HashMap<String, GeneralParameterValueIm>();

    /**
     * initializes a new <tt>ParameterValueGroup</tt>. The implementation of the
     * org.opengis.parameter.ParameterValueGroup interfaces adds mutator methods.
     * 
     * @param descriptor
     *            descriptor of this ParameterValueGroup
     * @param values
     *            values contaied in this group
     */
    public ParameterValueGroupIm( GeneralOperationParameterIm descriptor, GeneralParameterValueIm[] values ) {
        super( descriptor );
        setValues( values );
    }

    /**
     * 
     * @param name
     * @return named parameter
     * @throws InvalidParameterNameException
     */
    public GeneralParameterValueIm getValue( String name )
                            throws InvalidParameterNameException {
        return values.get( name );
    }

    /**
     * 
     * @return array of all parameters
     */
    public GeneralParameterValueIm[] getValues() {
        GeneralParameterValueIm[] gpv = new GeneralParameterValueIm[values.size()];
        return values.values().toArray( gpv );
    }

    /**
     * @param values 
     * @see #getValues()
     */
    public void setValues( GeneralParameterValueIm[] values ) {
        this.values.clear();
        for ( int i = 0; i < values.length; i++ ) {
            this.values.put( values[i].getDescriptor().getName(), values[i] );
        }
    }

    /**
     * @param value 
     * @see #getValues()
     */
    public void addValue( GeneralParameterValueIm value ) {
        values.put( value.getDescriptor().getName(), value );
    }

    /**
     * @param name
     */
    public void removeValue( String name ) {
        values.remove( name );
    }

}
