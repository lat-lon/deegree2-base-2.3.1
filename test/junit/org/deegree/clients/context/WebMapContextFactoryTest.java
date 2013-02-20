//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/clients/context/WebMapContextFactoryTest.java $
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
package org.deegree.clients.context;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.portal.context.Layer;
import org.deegree.portal.context.ViewContext;
import org.deegree.portal.context.WebMapContextFactory;

/**
 * 
 *
 * @version $Revision: 10877 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 *
 * @version 1.0. $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 *
 * @since 2.0
 */
public class WebMapContextFactoryTest extends TestCase {

    public static Test suite() {
        return new TestSuite(WebMapContextFactoryTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for WebMapContextFactoryTest
     * 
     * @param arg0
     */
    public WebMapContextFactoryTest(String arg0) {
        super(arg0);
    }

    /**
     * Create a ViewContext (web map context) from example configuration. <BR>
     * Steps:
     * <ul>
     * <li>read a configuration document
     * </ul>
     *  
     */
    public void testCreateViewContext() {
        fail( "Not tested because of missing resource -- fixme" );
        // try {
        // File file = new File( "C:/java/projekte/deegreeClient/webapps/client/WEB-INF/xml/atlas_l7o.xml" );
        // ViewContext vc =
        // WebMapContextFactory.createViewContext ( file.toURL(), null, null);
        // Layer[] layers = vc.getLayerList().getLayers();
        // if ( layers.length != 8 ) {
        // fail( "layer has not been read from the mapcontext docuemnt" );
        // }
        // } catch (Exception e) {
        //            fail( e.getMessage() );
        //        }
    }
    

}
