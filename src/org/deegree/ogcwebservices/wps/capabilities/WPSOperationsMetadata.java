//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/capabilities/WPSOperationsMetadata.java $
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

package org.deegree.ogcwebservices.wps.capabilities;

import java.util.ArrayList;
import java.util.List;

import org.deegree.ogcwebservices.getcapabilities.Operation;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;
import org.deegree.owscommon.OWSDomainType;

/**
 * WPSOperationsMetadata.java
 * 
 * Created on 08.03.2006. 19:26:51h
 * 
 * Metadata about the operations and related abilities specified by this service and implemented by
 * this server, including the URLs for operation requests. The basic contents of this section shall
 * be the same for all OWS types, but individual services can add elements and/or change the
 * optionality of optional elements.
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * @version 1.0.
 * @since 2.0
 */
public class WPSOperationsMetadata extends OperationsMetadata {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7688089087161206503L;

	/**
     * WPS describe process operation.
     */
    private Operation describeProcess;

    /**
     * WPS execute operation.
     */
    private Operation execute;

    /**
     * 
     */
    public static final String DESCRIBEPROCESS = "DescribeProcess";

    /**
     * 
     */
    public static final String EXECUTE = "Execute";

    /**
     * 
     * @param getCapabilitiesOperation
     * @param describeProcess
     * @param execute
     * @param parameters
     * @param constraints
     */
    public WPSOperationsMetadata( Operation getCapabilitiesOperation, Operation describeProcess, Operation execute,
                                  OWSDomainType[] parameters, OWSDomainType[] constraints ) {
        super( getCapabilitiesOperation, parameters, constraints );
        this.describeProcess = describeProcess;
        this.execute = execute;
    }

    /**
     * Returns all <code>Operations</code> known to the WPS.
     * 
     * @return all <code>Operations</code> known to the WPS.
     */
    @Override
    public Operation[] getOperations() {
        List<Operation> list = new ArrayList<Operation>( 3 );
        list.add( describeProcess );
        list.add( getCapabilitiesOperation );
        list.add( execute );
        Operation[] ops = new Operation[list.size()];
        return list.toArray( ops );
    }

    /**
     * @return Returns the describeProcess.
     */
    public Operation getDescribeProcess() {
        return describeProcess;
    }

    /**
     * @param describeProcess
     *            The describeProcess to set.
     */
    public void setDescribeProcess( Operation describeProcess ) {
        this.describeProcess = describeProcess;
    }

    /**
     * @return Returns the execute.
     */
    public Operation getExecute() {
        return execute;
    }

    /**
     * @param execute
     *            The execute to set.
     */
    public void setExecute( Operation execute ) {
        this.execute = execute;
    }

}
