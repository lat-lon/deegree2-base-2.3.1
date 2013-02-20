//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/operation/transaction/TransactionDocument.java $
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
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstraße 19
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
package org.deegree.ogcwebservices.wfs.operation.transaction;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureFactory;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.feature.GMLFeatureCollectionDocument;
import org.deegree.model.feature.GMLFeatureDocument;
import org.deegree.model.filterencoding.AbstractFilter;
import org.deegree.model.filterencoding.Filter;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcbase.PropertyPath;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.wfs.operation.AbstractWFSRequestDocument;
import org.deegree.ogcwebservices.wfs.operation.transaction.Insert.ID_GEN;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Parser for "wfs:Transaction" requests and contained elements.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10106 $, $Date: 2008-02-18 13:58:04 +0100 (Mon, 18 Feb 2008) $
 */
public class TransactionDocument extends AbstractWFSRequestDocument {
    
    private static ILogger LOG = LoggerFactory.getLogger( TransactionDocument.class );

    private static final long serialVersionUID = -394478447170286393L;

    private static final String XML_TEMPLATE = "TransactionTemplate.xml";

    private static final QualifiedName VALUE_ELEMENT_NAME = new QualifiedName( "wfs", "Value", CommonNamespaces.WFSNS );

    /**
     * Creates a skeleton document that contains the root element and the namespace bindings only.
     * 
     * @throws IOException
     * @throws SAXException
     */
    public void createEmptyDocument()
                            throws IOException, SAXException {
        URL url = TransactionDocument.class.getResource( XML_TEMPLATE );
        if ( url == null ) {
            throw new IOException( "The resource '" + XML_TEMPLATE + " could not be found." );
        }
        load( url );
    }

    /**
     * Parses the underlying document into a <code>Transaction</code> request object.
     * 
     * @param id
     * @return corresponding <code>Transaction</code> object
     * @throws XMLParsingException
     * @throws InvalidParameterValueException
     */
    public Transaction parse( String id )
                            throws XMLParsingException, InvalidParameterValueException {

        checkServiceAttribute();
        String version = checkVersionAttribute();

        Element root = this.getRootElement();
        String lockId = null;
        boolean releaseAllFeatures = parseReleaseActionParameter();

        List<TransactionOperation> operations = new ArrayList<TransactionOperation>();
        List<Element> list = XMLTools.getElements( root, "*", nsContext );
        for ( int i = 0; i < list.size(); i++ ) {
            Element element = list.get( i );
            if ( "LockId".equals( element.getLocalName() )
                 && CommonNamespaces.WFSNS.toString().equals( element.getNamespaceURI() ) ) {
                lockId = XMLTools.getNodeAsString( element, "text()", nsContext, null );
            } else {
                TransactionOperation operation = parseOperation( element );
                operations.add( operation );
            }
        }

        // vendorspecific attributes; required by deegree rights management
        Map<String, String> vendorSpecificParams = parseDRMParams( root );

        return new Transaction( id, version, vendorSpecificParams, lockId, operations, releaseAllFeatures, this );
    }

    /**
     * Parses the optional "releaseAction" attribute of the root element.
     * 
     * @return true, if releaseAction equals "ALL" (or is left out), false if it equals "SOME"
     * @throws InvalidParameterValueException
     *             if parameter
     * @throws XMLParsingException
     */
    private boolean parseReleaseActionParameter()
                            throws InvalidParameterValueException, XMLParsingException {

        String releaseAction = XMLTools.getNodeAsString( getRootElement(), "@releaseAction", nsContext, "ALL" );
        boolean releaseAllFeatures = true;
        if ( releaseAction != null ) {
            if ( "SOME".equals( releaseAction ) ) {
                releaseAllFeatures = false;
            } else if ( "ALL".equals( releaseAction ) ) {
                releaseAllFeatures = true;
            } else {
                throw new InvalidParameterValueException( "releaseAction", releaseAction );
            }
        }
        return releaseAllFeatures;
    }

    /**
     * Parses the given element as a <code>TransactionOperation</code>.
     * <p>
     * The given element must be one of the following:
     * <ul>
     * <li>wfs:Insert</li>
     * <li>wfs:Update</li>
     * <li>wfs:Delete</li>
     * <li>wfs:Native</li>
     * </ul>
     * 
     * @param element
     *            operation element
     * @return corresponding <code>TransactionOperation</code> object
     * @throws XMLParsingException
     */
    private TransactionOperation parseOperation( Element element )
                            throws XMLParsingException {

        TransactionOperation operation = null;

        if ( !element.getNamespaceURI().equals( CommonNamespaces.WFSNS.toString() ) ) {
            String msg = Messages.getMessage( "WFS_INVALID_OPERATION", element.getNodeName() );
            throw new XMLParsingException( msg );
        }
        if ( element.getLocalName().equals( "Insert" ) ) {
            operation = parseInsert( element );
        } else if ( element.getLocalName().equals( "Update" ) ) {
            operation = parseUpdate( element );
        } else if ( element.getLocalName().equals( "Delete" ) ) {
            operation = parseDelete( element );
        } else if ( element.getLocalName().equals( "Native" ) ) {
            operation = parseNative( element );
        } else {
            String msg = Messages.getMessage( "WFS_INVALID_OPERATION", element.getNodeName() );
            throw new XMLParsingException( msg );
        }

        return operation;
    }

    /**
     * Parses the given element as a "wfs:Insert" operation.
     * 
     * @param element
     *            "wfs:Insert" operation
     * @return corresponding Insert object
     * @throws XMLParsingException
     */
    private Insert parseInsert( Element element )
                            throws XMLParsingException {
        FeatureCollection fc = null;
        ID_GEN mode = parseIdGen( element );
        String handle = XMLTools.getNodeAsString( element, "@handle", nsContext, null );
        URI srsName = XMLTools.getNodeAsURI( element, "@srsName", nsContext, null );
        List<Element> childElementList = XMLTools.getRequiredElements( element, "*", nsContext );

        // either one _gml:FeatureCollection element or any number of _gml:Feature elements
        boolean isFeatureCollection = isFeatureCollection( childElementList.get( 0 ) );

        if ( isFeatureCollection ) {
            LOG.logDebug( "Insert (FeatureCollection)" );
            GMLFeatureCollectionDocument doc = new GMLFeatureCollectionDocument( false );
            doc.setRootElement( childElementList.get( 0 ) );
            doc.setSystemId( this.getSystemId() );
            fc = doc.parse();
        } else {
            LOG.logDebug( "Insert (Features)" );
            Feature[] features = new Feature[childElementList.size()];
            for ( int i = 0; i < childElementList.size(); i++ ) {
                try {
                    GMLFeatureDocument doc = new GMLFeatureDocument( false );
                    doc.setRootElement( childElementList.get( i ) );
                    doc.setSystemId( this.getSystemId() );
                    features[i] = doc.parseFeature();
                } catch ( Exception e ) {
                    throw new XMLParsingException( e.getMessage(), e );
                }
            }
            fc = FeatureFactory.createFeatureCollection( null, features );
        }

        return new Insert( handle, mode, srsName, fc );
    }

    /**
     * Checks whether the given element is a (concrete) gml:_FeatureCollection element.
     * <p>
     * NOTE: This check is far from perfect. Instead of determining the type of the element by inspecting the schema,
     * the decision is made by checking for child elements with name "gml:featureMember".
     * 
     * @param element
     *            potential gml:_FeatureCollection element
     * @return true, if the given element appears to be a gml:_FeatureCollection element, false otherwise
     * @throws XMLParsingException
     */
    private boolean isFeatureCollection( Element element )
                            throws XMLParsingException {
        boolean containsFeatureCollection = false;
        List<Node> nodeList = XMLTools.getNodes( element, "gml:featureMember", nsContext );
        if ( nodeList.size() > 0 ) {
            containsFeatureCollection = true;
        }
        return containsFeatureCollection;
    }

    /**
     * Parses the optional "idGen" attribute of the given "wfs:Insert" element.
     * 
     * @param element
     *            "wfs:Insert" element
     * @return "idGen" attribute code
     * @throws XMLParsingException
     */
    private ID_GEN parseIdGen( Element element )
                            throws XMLParsingException {
        ID_GEN mode;
        String idGen = XMLTools.getNodeAsString( element, "@idgen", nsContext, Insert.ID_GEN_GENERATE_NEW_STRING );

        if ( Insert.ID_GEN_GENERATE_NEW_STRING.equals( idGen ) ) {
            mode = Insert.ID_GEN.GENERATE_NEW;
        } else if ( Insert.ID_GEN_USE_EXISTING_STRING.equals( idGen ) ) {
            mode = Insert.ID_GEN.USE_EXISTING;
        } else if ( Insert.ID_GEN_REPLACE_DUPLICATE_STRING.equals( idGen ) ) {
            mode = Insert.ID_GEN.REPLACE_DUPLICATE;
        } else {
            String msg = Messages.getMessage( "WFS_INVALID_IDGEN_VALUE", idGen, Insert.ID_GEN_GENERATE_NEW_STRING,
                                              Insert.ID_GEN_REPLACE_DUPLICATE_STRING, Insert.ID_GEN_USE_EXISTING_STRING );
            throw new XMLParsingException( msg );
        }
        return mode;
    }

    /**
     * Parses the given element as a "wfs:Update" operation.
     * 
     * @param element
     *            "wfs:Update" operation
     * @return corresponding Update object
     * @throws XMLParsingException
     */
    private Update parseUpdate( Element element )
                            throws XMLParsingException {

        Update update = null;
        String handle = XMLTools.getNodeAsString( element, "@handle", nsContext, null );
        QualifiedName typeName = XMLTools.getRequiredNodeAsQualifiedName( element, "@typeName", nsContext );

        Element filterElement = (Element) XMLTools.getNode( element, "ogc:Filter", nsContext );
        Filter filter = null;
        if ( filterElement != null ) {
            filter = AbstractFilter.buildFromDOM( filterElement );
        }

        List<Node> properties = XMLTools.getNodes( element, "wfs:Property", nsContext );
        if ( properties.size() > 0 ) {
            // "standard" update (specifies properties + their replacement values)
            LOG.logDebug( "Update (replacement properties)" );
            Map<PropertyPath, FeatureProperty> replacementProps = new LinkedHashMap<PropertyPath, FeatureProperty>();
            Map<PropertyPath, Node> rawProps = new LinkedHashMap<PropertyPath, Node>();
            for ( int i = 0; i < properties.size(); i++ ) {
                Node propNode = properties.get( i );
                Text propNameNode = (Text) XMLTools.getRequiredNode( propNode, "wfs:Name/text()", nsContext );
                PropertyPath propPath = parsePropertyPath( propNameNode );

                // TODO remove this (only needed, because Update makes the DOM representation available)
                Node valueNode = XMLTools.getNode( propNode, "wfs:Value/*", nsContext );
                if ( valueNode == null ) {
                    valueNode = XMLTools.getNode( propNode, "wfs:Value/text()", nsContext );
                }
                rawProps.put( propPath, propNode );

                FeatureProperty replacementProp = null;
                QualifiedName propName = propPath.getStep( propPath.getSteps() - 1 ).getPropertyName();

                if ( replacementProps.get( propPath ) != null ) {
                    String msg = Messages.getMessage( "WFS_UPDATE_DUPLICATE_PROPERTY", handle, propPath );
                    throw new XMLParsingException( msg );
                }

                // TODO improve generation of FeatureProperty from "wfs:Value" element
                Object propValue = null;
                Node node = XMLTools.getNode( propNode, "wfs:Value", nsContext );
                if ( node != null ) {
                    GMLFeatureDocument dummyDoc = new GMLFeatureDocument( false );
                    dummyDoc.setRootElement( (Element) propNode );
                    dummyDoc.setSystemId( this.getSystemId() );
                    Feature dummyFeature;
                    try {
                        dummyFeature = dummyDoc.parseFeature();
                    } catch ( UnknownCRSException e ) {
                        throw new XMLParsingException( e.getMessage(), e );
                    }
                    propValue = dummyFeature.getProperties( VALUE_ELEMENT_NAME )[0].getValue();
                }
                replacementProp = FeatureFactory.createFeatureProperty( propName, propValue );
                replacementProps.put( propPath, replacementProp );
            }
            update = new Update( handle, typeName, replacementProps, rawProps, filter );
        } else {
            // deegree specific update (specifies a single replacement feature)
            LOG.logDebug( "Update (replacement feature)" );
            Feature replacementFeature = null;
            List<Element> childElementList = XMLTools.getRequiredElements( element, "*", nsContext );
            if ( ( filter == null && childElementList.size() != 1 )
                 || ( filter != null && childElementList.size() != 2 ) ) {
                String msg = Messages.getMessage( "WFS_UPDATE_FEATURE_REPLACE", handle );
                throw new XMLParsingException( msg );
            }
            try {
                GMLFeatureDocument doc = new GMLFeatureDocument( false );
                doc.setRootElement( childElementList.get( 0 ) );
                doc.setSystemId( this.getSystemId() );
                replacementFeature = doc.parseFeature();
            } catch ( Exception e ) {
                String msg = Messages.getMessage( "WFS_UPDATE_FEATURE_REPLACE", handle );
                throw new XMLParsingException( msg );
            }
            update = new Update( handle, typeName, replacementFeature, filter );
        }

        return update;
    }

    /**
     * Parses the given element as a "wfs:Delete" operation.
     * 
     * @param element
     *            "wfs:Delete" operation
     * @return corresponding Delete object
     */
    private Delete parseDelete( Element element )
                            throws XMLParsingException {

        String handle = XMLTools.getNodeAsString( element, "@handle", nsContext, null );
        QualifiedName typeName = XMLTools.getRequiredNodeAsQualifiedName( element, "@typeName", nsContext );

        Element filterElement = (Element) XMLTools.getRequiredNode( element, "ogc:Filter", nsContext );
        Filter filter = AbstractFilter.buildFromDOM( filterElement );
        return new Delete( handle, typeName, filter );
    }

    /**
     * Parses the given element as a "wfs:Native" operation.
     * 
     * @param element
     *            "wfs:Native" operation
     * @return corresponding Native object
     */
    private Native parseNative( Element element )
                            throws XMLParsingException {
        String handle = XMLTools.getNodeAsString( element, "@handle", nsContext, null );
        String vendorID = XMLTools.getRequiredNodeAsString( element, "@vendorId", nsContext );
        boolean safeToIgnore = XMLTools.getRequiredNodeAsBoolean( element, "@safeToIgnore", nsContext );
        return new Native( handle, element, vendorID, safeToIgnore );
    }
}