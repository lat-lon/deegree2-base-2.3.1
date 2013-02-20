//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/getcapabilities/ServiceOperation.java $
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
package org.deegree.ogcwebservices.getcapabilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ServiceOperation
 * 
 * @author Administrator
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class ServiceOperation {

    private List dcpList;

    public ServiceOperation() {
        this.dcpList = new ArrayList();
    }

    public DCPType[] getDCPTypes( Protocol protocol ) {
        DCPType[] typeArray;
        List returnTypeList = new ArrayList();
        Iterator iterator = dcpList.iterator();
        while ( iterator.hasNext() ) {
            DCPType element = (DCPType) iterator.next();
            if ( element.getProtocol().equals( protocol ) ) {
                returnTypeList.add( element );
            }
        }
        typeArray = new DCPType[returnTypeList.size()];
        return (DCPType[]) returnTypeList.toArray( typeArray );
    }

    /**
     * Set all DCP types. First empyt list, then sets
     * 
     * @param types
     */
    public void setDCPTypes( DCPType[] types ) {
        this.dcpList.clear();
        for ( int i = 0; i < types.length; i++ ) {
            this.addDCPType( types[i] );
        }
    }

    public void addDCPType( DCPType type ) {
        this.dcpList.add( type );
    }

}
