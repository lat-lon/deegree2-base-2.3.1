//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/enterprise/servlet/WFSHandlerTest.java $
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
package org.deegree.enterprise.servlet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test with mock object to perform http get/post request.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9336 $, $Date: 2007-12-27 12:31:18 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class WFSHandlerTest extends TestCase {

    public static Test suite() {
        return new TestSuite( WFSHandlerTest.class );
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp()
                            throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown()
                            throws Exception {
        super.tearDown();
    }

    public void testPerform() {
        WFSHandler handler = new WFSHandler();
        assertNotNull( handler );
        // handler.perform();
    }

}

/***************************************************************************************************
 * <code>
 Changes to this class. What the people have been up to:

 $Log$
 Revision 1.4  2007/02/12 09:34:57  wanhoff
 added footer, corrected header

 </code>
 **************************************************************************************************/
