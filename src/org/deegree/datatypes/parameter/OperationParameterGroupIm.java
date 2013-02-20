// $HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/parameter/OperationParameterGroupIm.java $
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
 * 
 * 
 * @version $Revision: 9337 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9337 $, $Date: 2007-12-27 13:31:11 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 * @deprecated Not required. Will be deleted.
 */
public class OperationParameterGroupIm extends GeneralOperationParameterIm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, GeneralOperationParameterIm> parameter = new HashMap<String, GeneralOperationParameterIm>();

    /**
     * @param identifiers
     * @param name
     * @param remarks
     * @param maximumOccurs
     * @param minimumOccurs
     * @param parameter
     */
    public OperationParameterGroupIm( String name, String remarks, int maximumOccurs, int minimumOccurs,
                                      GeneralOperationParameterIm[] parameter ) {
        super( name, remarks, maximumOccurs, minimumOccurs );
        setParameter( parameter );
    }

    /**
     * 
     * @param name
     * @return name parameter
     * @throws InvalidParameterNameException
     */
    public GeneralOperationParameterIm getParameter( String name )
                            throws InvalidParameterNameException {
        return parameter.get( name );
    }

    /**
     * 
     * @return array of all parameters
     */
    public GeneralOperationParameterIm[] getParameters() {
        GeneralOperationParameterIm[] gop = new GeneralOperationParameterIm[parameter.size()];
        return parameter.values().toArray( gop );
    }

    /**
     * @param parameter
     */
    public void setParameter( GeneralOperationParameterIm[] parameter ) {
        this.parameter.clear();
        for ( int i = 0; i < parameter.length; i++ ) {
            this.parameter.put( parameter[i].getName(), parameter[i] );
        }
    }

    /**
     * @param parameter
     */
    public void addParameter( GeneralOperationParameterIm parameter ) {
        this.parameter.put( parameter.getName(), parameter );
    }

    /**
     * @param name
     */
    public void removeParameter( String name ) {
        parameter.remove( name );
    }

}
