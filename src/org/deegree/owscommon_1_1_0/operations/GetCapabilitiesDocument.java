//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/operations/GetCapabilitiesDocument.java $
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

package org.deegree.owscommon_1_1_0.operations;

import static org.deegree.framework.xml.XMLTools.getElement;
import static org.deegree.framework.xml.XMLTools.getNodesAsStringList;
import static org.deegree.ogcbase.CommonNamespaces.OWS_1_1_0PREFIX;

import java.util.ArrayList;
import java.util.List;

import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.w3c.dom.Element;

/**
 * <code>GetCapabilitiesDocument</code> parses the OWSCommon 1.1.0 part of a
 * <code>GetCapabilities</code> request.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10830 $, $Date: 2008-03-31 11:33:56 +0200 (Mo, 31. Mär 2008) $
 * 
 */
public class GetCapabilitiesDocument extends XMLFragment {

    private static final long serialVersionUID = -4559202452840295387L;

    private final static String PRE = OWS_1_1_0PREFIX + ":";

    /**
     * 
     * @return a list of versions the client requested or an empty list if no versions were given.
     * @throws XMLParsingException
     *             if the node could not be parsed.
     */
    public List<String> parseAcceptVersions()
                            throws XMLParsingException {
        Element root = getRootElement();
        List<String> result = new ArrayList<String>();
        if ( root != null ) {
            Element acceptVersions = getElement( root, PRE + "AcceptVersions", nsContext );
            if ( acceptVersions != null ) {
                result = getNodesAsStringList( acceptVersions, PRE + "Version", nsContext );
            }
        }
        return result;
    }

    /**
     * @return a list of sections the client requested or an empty list if no sections were given.
     * @throws XMLParsingException
     *             if the node could not be parsed.
     */
    public List<String> parseSections()
                            throws XMLParsingException {
        Element root = getRootElement();
        List<String> result = new ArrayList<String>();
        if ( root != null ) {
            Element sections = getElement( root, PRE + "Sections", nsContext );
            if ( sections != null ) {
                result = getNodesAsStringList( sections, PRE + "Section", nsContext );
            }
        }
        return result;
    }

    /**
     * @return a list of AcceptedFormats the client requested or an empty list if no AcceptedFormats
     *         were given.
     * @throws XMLParsingException
     *             if the node could not be parsed.
     */
    public List<String> parseAcceptFormats()
                            throws XMLParsingException {
        Element root = getRootElement();
        List<String> result = new ArrayList<String>();
        if ( root != null ) {
            Element acceptFormats = getElement( root, PRE + "AcceptFormats", nsContext );
            if ( acceptFormats != null ) {
                result = getNodesAsStringList( acceptFormats, PRE + "OutputFormat", nsContext );
            }
        }
        return result;
    }

    /**
     * 
     * @return the update sequence string or an empty string if not given.
     */
    public String parseUpdateSequence() {
        Element root = getRootElement();
        String result = new String();
        if ( root != null ) {
            result = root.getAttribute( "updateSequence" );
        }
        return result;
    }

}
