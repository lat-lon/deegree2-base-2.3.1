//$HeadURL$
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

package org.deegree.ogcwebservices.wcs.configuration;

import java.util.List;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class ScriptResolution extends AbstractResolution {

    private String script;

    private List<String> parameter;

    private String resultFormat;

    private String storageLocation;

    /**
     * @param minScale
     * @param maxScale
     * @param range
     * @param script
     * @param parameter
     * @param resultFormat
     * @param storageLocation
     * @throws IllegalArgumentException
     */
    public ScriptResolution( double minScale, double maxScale, Range[] range, String script, List<String> parameter,
                             String resultFormat, String storageLocation ) throws IllegalArgumentException {
        super( minScale, maxScale, range );
        this.script = script;
        this.parameter = parameter;
        this.resultFormat = resultFormat;
        this.storageLocation = storageLocation;
    }

    /**
     * 
     * @return reference to script (absolute path)
     */
    public String getScript() {
        return script;
    }

    /**
     * 
     * @return parameter to pass to the script
     */
    public List<String> getParameter() {
        return parameter;
    }

    /**
     * 
     * @return (image) format of the result produced by the script
     */
    public String getResultFormat() {
        return resultFormat;
    }

    /**
     * 
     * @return absolute path to directory where result of script invocation shall be stored for
     *         further processing
     */
    public String getStorageLocation() {
        return storageLocation;
    }

}
