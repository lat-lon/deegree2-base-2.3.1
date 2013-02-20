//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/OperationsMetadata.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
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

package org.deegree.owscommon_1_1_0;

import java.util.List;

import org.w3c.dom.Element;

/**
 * <code>OperationsMetadata</code> encapsulates the ows 1.1.0 operations metadata section of a
 * capabilities document.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10830 $, $Date: 2008-03-31 11:33:56 +0200 (Mon, 31 Mar 2008) $
 * 
 */

public class OperationsMetadata {

    private final List<Operation> operations;

    private final List<DomainType> parameters;

    private final List<DomainType> constraints;

    private final Element extendedCapabilities;

    /**
     * @param operations
     *            data about the operations
     * @param parameters
     *            metadata about params
     * @param constraints
     *            metadata about constraints.
     * @param extendedCapabilities
     *            anytype, therefore saving as xml element.
     */
    public OperationsMetadata( List<Operation> operations, List<DomainType> parameters, List<DomainType> constraints,
                               Element extendedCapabilities ) {
        this.operations = operations;
        this.parameters = parameters;
        this.constraints = constraints;
        this.extendedCapabilities = extendedCapabilities;
    }

    /**
     * @return the operations.
     */
    public final List<Operation> getOperations() {
        return operations;
    }

    /**
     * @return the parameters.
     */
    public final List<DomainType> getParameters() {
        return parameters;
    }

    /**
     * @return the constraints.
     */
    public final List<DomainType> getConstraints() {
        return constraints;
    }

    /**
     * @return the extendedCapabilities.
     */
    public final Element getExtendedCapabilities() {
        return extendedCapabilities;
    }

}
