//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/manager/Insert.java $
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
package org.deegree.ogcwebservices.csw.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * An Insert object is a container for one or more records that are to be inserted into the
 * catalogue. The schema of the record(s) must conform to the schema of the information model that
 * the catalogue supports as described using the DescribeRecord operation.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 */
public class Insert extends Operation {

    private List<Element> records = null;

    private Map<String, Element> extrinsicObjects;

    /**
     * 
     * @param handle
     *            attribute of the insert.
     * @param records
     *            a list containing all (firstlevel) child xml elements beneath the <csw:insert>
     *            element
     * 
     */
    public Insert( String handle, List<Element> records ) {
        super( "Insert", handle );
        this.records = records;
        this.extrinsicObjects = new HashMap<String, Element>();
    }

    /**
     * @param handle
     *            attribute of the insert.
     * @param records
     *            a list containing all (firstlevel) child xml elements beneath the <csw:insert>
     *            element
     * @param extrinsicObjects
     *            a mapping of the lid of the values to the rim:ExtrinsicObject w3c-element. They
     *            are needed to find the actual extrinsic objects into the multiparts send to the
     *            csw.
     * 
     */
    public Insert( String handle, List<Element> records, Map<String, Element> extrinsicObjects ) {
        super( "Insert", handle );
        this.records = records;
        if( extrinsicObjects != null ){
            this.extrinsicObjects = extrinsicObjects;
        } else {
            this.extrinsicObjects = new HashMap<String, Element>();
        }
    }

    /**
     * returns records to insert
     * 
     * @return records to insert
     */
    public List<Element> getRecords() {
        return records;
    }

    /**
     * @return the ebrim extrinsicObjects which should be inserted or an empty map if no
     *         ExtrinsicObject should be inserted, or no ebrim is used in this catalogue.
     */
    public Map<String, Element> getExtrinsicObjects() {
        return extrinsicObjects;
    }

}