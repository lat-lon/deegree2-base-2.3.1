//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wfs/configuration/WFSConfigurationDocumentTest.java $
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
package org.deegree.ogcwebservices.wfs.configuration;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.InvalidConfigurationException;
import org.deegree.ogcwebservices.wfs.XMLFactory;
import org.xml.sax.SAXException;

import alltests.Configuration;

/**
 * Validate the parser methods for a WFS capabilities document.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 * 
 */
public class WFSConfigurationDocumentTest extends TestCase {
    private static ILogger LOG = LoggerFactory.getLogger( WFSConfigurationDocumentTest.class );

    public static Test suite() {
        return new TestSuite( WFSConfigurationDocumentTest.class );
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

    public void testGetCapabilities()
                            throws SAXException, IOException, InvalidConfigurationException {
        WFSConfigurationDocument confDoc = new WFSConfigurationDocument();
        confDoc.load( Configuration.getWFSConfigurationURL() );
        WFSConfiguration configuration = confDoc.getConfiguration();
        assertNotNull( configuration );
        LOG.logInfo( XMLFactory.export( configuration ).getAsPrettyString() );
    }

    public void testGetDeegreeParams()
                            throws InvalidConfigurationException, IOException, SAXException {
        WFSConfigurationDocument confDoc = new WFSConfigurationDocument();
        confDoc.load( Configuration.getWFSConfigurationURL() );
        WFSConfiguration config = confDoc.getConfiguration();
        WFSDeegreeParams deegreeParams = config.getDeegreeParams();
        assertNotNull( deegreeParams );
        assertEquals( deegreeParams.getDefaultOnlineResource().getLinkage().getHref().toString(),
                      "http://127.0.0.1:8080/deegree/services" );
        assertEquals( deegreeParams.getRequestTimeLimit(), 120 );
        assertEquals( deegreeParams.getCharacterSet(), "UTF-8" );
        String[] dataDirectories = deegreeParams.getDataDirectories();
        assertNotNull( dataDirectories );
        assertEquals( dataDirectories.length, 1 );
    }
}
