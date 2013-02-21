//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/saml/Assertion.java $
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

package org.deegree.ogcwebservices.wass.saml;

import java.util.ArrayList;
import java.util.Date;

/**
 * Encapsulated data: Assertion element
 * 
 * Namespace: http://urn:oasis:names:tc.SAML:1.0:assertion
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class Assertion {

    private Conditions conditions = null;

    private ArrayList<Assertion> advices = null;

    private String[] adviceIDs = null;

    private ArrayList<Statement> statements = null;

    private int majorVersion = 0;

    private int minorVersion = 0;

    private String assertionID = null;

    private String issuer = null;

    private Date issueInstant = null;

    /**
     * @param conditions
     * @param advices
     * @param adviceIDs
     * @param statements
     * @param majorVersion
     * @param minorVersion
     * @param assertionID
     * @param issuer
     * @param issueInstant
     */
    public Assertion( Conditions conditions, ArrayList<Assertion> advices, String[] adviceIDs,
                     ArrayList<Statement> statements, int majorVersion, int minorVersion,
                     String assertionID, String issuer, Date issueInstant ) {
        this.conditions = conditions;
        this.advices = advices;
        this.adviceIDs = adviceIDs;
        this.statements = statements;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.assertionID = assertionID;
        this.issuer = issuer;
        this.issueInstant = issueInstant;
    }

    /**
     * @return Returns the adviceIDs.
     */
    public String[] getAdviceIDs() {
        return adviceIDs;
    }

    /**
     * @return Returns the advices.
     */
    public ArrayList<Assertion> getAdvices() {
        return advices;
    }

    /**
     * @return Returns the assertionID.
     */
    public String getAssertionID() {
        return assertionID;
    }

    /**
     * @return Returns the conditions.
     */
    public Conditions getConditions() {
        return conditions;
    }

    /**
     * @return Returns the issueInstant.
     */
    public Date getIssueInstant() {
        return issueInstant;
    }

    /**
     * @return Returns the issuer.
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @return Returns the majorVersion.
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * @return Returns the minorVersion.
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * @return Returns the statements.
     */
    public ArrayList<Statement> getStatements() {
        return statements;
    }

}
