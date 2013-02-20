//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/model/filterencoding/capabilities/FilterCapabilitiesTest.java $
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
package org.deegree.model.filterencoding.capabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.framework.xml.XMLParsingException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import alltests.Configuration;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version. $Revision: 9336 $, $Date: 2007-12-27 12:31:18 +0100 (Thu, 27 Dec 2007) $
 */
public class FilterCapabilitiesTest extends TestCase {

    private static final String SAMPLE_DIR = "resources/filter/1.1.0/";

    private static final String CAPABILITIES_SAMPLE = SAMPLE_DIR + "Filter_Capabilities_Sample.xml";

    private static final String CAPABILITIES_SAMPLE_SMALL = SAMPLE_DIR + "Filter_Capabilities_Small_Sample.xml";

    public static Test suite() {
        return new TestSuite( FilterCapabilitiesTest.class );
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

    public static FilterCapabilities getTestInstance() {
        return new FilterCapabilities(
                                       new ScalarCapabilities( true, new Operator[] { new Operator( "Like" ) },
                                                               new Operator[] { new Operator( "Simple_Arithmetic" ) } ),
                                       new SpatialCapabilities( new SpatialOperator[] { new SpatialOperator( "BBOX",
                                                                                                             null ) } ),
                                       new IdCapabilities( new Element[0], new Element[0] ) );
    }

    public void testLoadSampleCapabilitiesSmall()
                            throws MalformedURLException, IOException, SAXException, XMLParsingException {
        FilterCapabilities110Fragment capabilitiesDocument = new FilterCapabilities110Fragment(
                                                                                                new URL(
                                                                                                         Configuration.getBaseDir(),
                                                                                                         CAPABILITIES_SAMPLE_SMALL ) );
        capabilitiesDocument.parseFilterCapabilities();

    }

    public void testLoadSampleCapabilities()
                            throws MalformedURLException, IOException, SAXException, XMLParsingException {
        FilterCapabilities110Fragment capabilitiesDocument = new FilterCapabilities110Fragment(
                                                                                                new URL(
                                                                                                         Configuration.getBaseDir(),
                                                                                                         CAPABILITIES_SAMPLE ) );
        capabilitiesDocument.parseFilterCapabilities();

    }
}