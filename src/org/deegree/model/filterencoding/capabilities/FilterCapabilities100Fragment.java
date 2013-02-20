// $HeadURL:
// /deegreerepository/deegree/src/org/deegree/model/filterencoding/capabilities/FilterCapabilities100Factory.java,v
// 1.3 2005/03/09 11:55:46 mschneider Exp $
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

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.ElementList;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.getcapabilities.UnknownOperatorNameException;
import org.w3c.dom.Element;

/**
 * 
 * 
 * 
 * @version $Revision: 10087 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 10087 $, $Date: 2008-02-15 14:56:28 +0100 (Fri, 15 Feb 2008) $
 * 
 * @since 2.0
 */
public class FilterCapabilities100Fragment extends XMLFragment {

    private static final URI OGCNS = CommonNamespaces.OGCNS;

    private static final ILogger LOG = LoggerFactory.getLogger( FilterCapabilities100Fragment.class );

    private static NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    /**
     * Creates a new <code>FilterCapabilities100Fragment</code> from the given parameters.
     * 
     * @param element
     * @param systemId
     */
    public FilterCapabilities100Fragment( Element element, URL systemId ) {
        super( element );
        setSystemId( systemId );
    }

    /**
     * Returns the object representation for the <code>ogc:Filter_Capabilities</code> root
     * element.
     * 
     * @return object representation for the given <code>ogc:Filter_Capabilities</code> element
     * @throws XMLParsingException
     */
    public FilterCapabilities parseFilterCapabilities()
                            throws XMLParsingException {
        Element e1 = (Element) XMLTools.getRequiredNode( getRootElement(), "ogc:Scalar_Capabilities", nsContext );
        Element e2 = (Element) XMLTools.getRequiredNode( getRootElement(), "ogc:Spatial_Capabilities", nsContext );
        return new FilterCapabilities( parseScalarCapabilities( e1 ), parseSpatialCapabilities( e2 ) );
    }

    /**
     * Returns the object representation for an <code>ogc:Spatial_Capabilities</code> element.
     * 
     * @return object representation for the given <code>ogc:Spatial_Capabilities</code> element
     * @throws XMLParsingException
     */
    private SpatialCapabilities parseSpatialCapabilities( Element spatialElement )
                            throws XMLParsingException {
        Map<String, Element> operatorMap = parseOperators( (Element) XMLTools.getRequiredNode( spatialElement,
                                                                                               "ogc:Spatial_Operators",
                                                                                               nsContext ) );
        ArrayList<SpatialOperator> operators = new ArrayList<SpatialOperator>();
        Iterator<String> it = operatorMap.keySet().iterator();
        while ( it.hasNext() ) {
            try {
                operators.add( OperatorFactory100.createSpatialOperator( it.next() ) );
            } catch ( UnknownOperatorNameException e ) {
                throw new XMLParsingException( "Unknown operator name in 'ogc:Spatial_Operators' element encountered: "
                                               + e.getMessage(), e );
            }
        }
        return new SpatialCapabilities( operators.toArray( new SpatialOperator[operators.size()] ) );
    }

    /**
     * Returns the object representation for an <code>ogc:Scalar_Capabilities</code> element.
     * 
     * @return object representation for the given <code>ogc:Scalar_Capabilities</code> element
     * @throws XMLParsingException
     */
    private ScalarCapabilities parseScalarCapabilities( Element scalarElement )
                            throws XMLParsingException {

        // "Logical_Operators"-element
        boolean supportsLogicalOperators = false;
        if ( XMLTools.getChildElement( "Logical_Operators", OGCNS, scalarElement ) != null ) {
            supportsLogicalOperators = true;
        }

        // "Comparison_Operators"-element
        Element elem = XMLTools.getRequiredChildElement( "Comparison_Operators", OGCNS, scalarElement );
        Map<String, Element> operatorMap = parseOperators( elem );
        ArrayList<Operator> operators = new ArrayList<Operator>();
        Iterator<String> it = operatorMap.keySet().iterator();
        while ( it.hasNext() ) {
            String next = it.next();
            try {
                operators.add( OperatorFactory100.createComparisonOperator( next ) );
            } catch ( UnknownOperatorNameException e ) {
                LOG.logWarning( "Operator name not found. Trying again with filter encoding 1.1.0 names..." );
                try {
                    operators.add( OperatorFactory110.createComparisonOperator( next ) );
                } catch ( UnknownOperatorNameException e2 ) {
                    LOG.logError( "Still not found. Here's two stack traces:" );
                    LOG.logError( e.getMessage(), e );
                    LOG.logError( e2.getMessage(), e2 );
                }
            }
        }
        Operator[] comparionsOperators = operators.toArray( new Operator[operators.size()] );

        // "Arithmetic_Operators"-element
        elem = XMLTools.getRequiredChildElement( "Arithmetic_Operators", OGCNS, scalarElement );
        operatorMap = parseOperators( elem );
        operators = new ArrayList<Operator>();
        it = operatorMap.keySet().iterator();
        while ( it.hasNext() ) {
            try {
                String operatorName = it.next();
                if ( operatorName.equals( OperatorFactory100.OPERATOR_FUNCTIONS ) ) {
                    // functions definition
                    Element functionsElement = operatorMap.get( operatorName );
                    Element functionNamesElement = XMLTools.getRequiredChildElement( "Function_Names", OGCNS,
                                                                                     functionsElement );
                    List functionNameList = XMLTools.getRequiredNodes( functionNamesElement, "ogc:Function_Name",
                                                                       nsContext );
                    for ( int i = 0; i < functionNameList.size(); i++ ) {
                        Element functionNameElement = (Element) functionNameList.get( i );
                        String name = XMLTools.getStringValue( functionNameElement );
                        String argumentCount = XMLTools.getRequiredAttrValue( "nArgs", null, functionNameElement );
                        if ( name == null || name.length() == 0 ) {
                            throw new XMLParsingException( "Error parsing a 'Function_Name' (namespace: '" + OGCNS
                                                           + "') element: text node is empty." );
                        }
                        try {
                            operators.add( OperatorFactory100.createArithmeticFunction(
                                                                                        name,
                                                                                        Integer.parseInt( argumentCount ) ) );
                        } catch ( NumberFormatException e ) {
                            throw new XMLParsingException( "Error parsing 'Function_Name' (namespace: '" + OGCNS
                                                           + "') element: attribute 'nArgs'"
                                                           + " does not contains a valid integer value." );
                        }
                    }

                } else {
                    // simple operator
                    operators.add( OperatorFactory100.createArithmeticOperator( operatorName ) );
                }
            } catch ( UnknownOperatorNameException e ) {
                LOG.logError( e.getMessage(), e );
            }
        }
        Operator[] arithmeticOperators = operators.toArray( new Operator[operators.size()] );
        return new ScalarCapabilities( supportsLogicalOperators, comparionsOperators, arithmeticOperators );
    }

    private Map<String, Element> parseOperators( Element operatorsElement ) {
        HashMap<String, Element> operators = new HashMap<String, Element>();
        ElementList operatorList = XMLTools.getChildElements( operatorsElement );
        for ( int i = 0; i < operatorList.getLength(); i++ ) {
            String namespaceURI = operatorList.item( i ).getNamespaceURI();
            if ( namespaceURI != null && namespaceURI.equals( OGCNS.toASCIIString() ) ) {
                operators.put( operatorList.item( i ).getLocalName(), operatorList.item( i ) );
            }
        }
        return operators;
    }
}
