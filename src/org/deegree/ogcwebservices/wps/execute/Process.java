//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/execute/Process.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
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
package org.deegree.ogcwebservices.wps.execute;

import java.util.Map;

import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wps.describeprocess.ProcessDescription;
import org.deegree.ogcwebservices.wps.execute.ExecuteResponse.ProcessOutputs;

/**
 * Process.java
 * 
 * Created on 11.03.2006. 18:32:39h
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * @author last edited by: $Author: otonnhofer $
 * 
 * @version $Revision: 11022 $, $Date: 2008-04-10 17:59:28 +0200 (Do, 10. Apr 2008) $
 */
public abstract class Process {

    /**
     * 
     */
    protected ProcessDescription processDescription;

    /**
     * 
     * @param processDescription
     */
    public Process( ProcessDescription processDescription ) {
        this.processDescription = processDescription;
    }

    /**
     * 
     * @param inputs
     * @param outputDefinitions
     * @return the result of the process
     * @throws OGCWebServiceException
     */
    public abstract ProcessOutputs execute( Map<String, IOValue> inputs,
                                            OutputDefinitions outputDefinitions )
                            throws OGCWebServiceException;

    /**
     * 
     * @return processDescription
     */
    public ProcessDescription getProcessDescription() {
        return this.processDescription;
    }

}

