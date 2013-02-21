//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/manager/TransactionResult.java $
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

import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcwebservices.DefaultOGCWebServiceResponse;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.w3c.dom.Element;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 11965 $, $Date: 2008-05-28 16:55:15 +0200 (Mi, 28. Mai 2008) $
 */
public class TransactionResult extends DefaultOGCWebServiceResponse {

    private int totalInserted = 0;

    private int totalDeleted = 0;

    private int totalUpdated = 0;

    private InsertResults results = null;

    /**
     * 
     * @param transaction
     * @param root
     * @return the result
     * @throws XMLParsingException
     */
    public static final TransactionResult create( Transaction transaction, Element root )
                            throws XMLParsingException {
        TransactionResultDocument doc = new TransactionResultDocument();
        doc.setRootElement( root );
        return doc.parseTransactionResponse( transaction );
    }

    /**
     * 
     * @param transaction
     *            source Transaction request
     * @param totalInserted
     *            the amount of records that has been inserted
     * @param totalDeleted
     *            the amount of records that has been deleted
     * @param totalUpdated
     *            the amount of records that has been updated
     * @param results
     *            insert result description
     */
    public TransactionResult( OGCWebServiceRequest transaction, int totalInserted, int totalDeleted, int totalUpdated,
                              InsertResults results ) {
        super( transaction );
        this.totalInserted = totalInserted;
        this.totalDeleted = totalDeleted;
        this.totalUpdated = totalUpdated;
        this.results = results;
    }

    /**
     * returns insert result description
     * 
     * @return insert result description
     */
    public InsertResults getResults() {
        return results;
    }

    /**
     * returns the amount of records that has been deleted
     * 
     * @return the amount of records that has been deleted
     */
    public int getTotalDeleted() {
        return totalDeleted;
    }

    /**
     * returns the amount of records that has been inserted
     * 
     * @return the amount of records that has been inserted
     */
    public int getTotalInserted() {
        return totalInserted;
    }

    /**
     * returns the amount of records that has been updated
     * 
     * @return the amount of records that has been updated
     */
    public int getTotalUpdated() {
        return totalUpdated;
    }

}