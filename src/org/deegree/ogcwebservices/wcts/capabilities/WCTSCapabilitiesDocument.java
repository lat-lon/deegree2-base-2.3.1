//$HeadURL: $
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

package org.deegree.ogcwebservices.wcts.capabilities;

import static org.deegree.framework.xml.XMLTools.getElement;
import static org.deegree.framework.xml.XMLTools.getElements;
import static org.deegree.framework.xml.XMLTools.getNodeAsBoolean;
import static org.deegree.framework.xml.XMLTools.getNodesAsStringList;
import static org.deegree.framework.xml.XMLTools.getRequiredElements;
import static org.deegree.framework.xml.XMLTools.getRequiredNodeAsBoolean;
import static org.deegree.framework.xml.XMLTools.getStringValue;
import static org.deegree.ogcbase.CommonNamespaces.WCS_1_2_0_PREFIX;
import static org.deegree.ogcbase.CommonNamespaces.WCTS_PREFIX;

import java.util.ArrayList;
import java.util.List;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.Pair;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.wcts.WCTService;
import org.deegree.owscommon_1_1_0.OWSCommonCapabilitiesDocument;
import org.w3c.dom.Element;

/**
 * <code>WCTSCapabilitiesDocument</code> parses a given wcts:Capabilities document version 0.4.0, with ows:Common
 * 1.1.0 and csw 1.2.0.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class WCTSCapabilitiesDocument extends OWSCommonCapabilitiesDocument {

    /**
     * 
     */
    private static final long serialVersionUID = -2378224055294207801L;

    private static ILogger LOG = LoggerFactory.getLogger( WCTSCapabilitiesDocument.class );

    private static String PRE = WCTS_PREFIX + ":";

    // Default value of codeType attribute, from wctsCommon.xsd.
    private static String DEFAULT_COV_URL = "http://schemas.opengis.net/wcts/0.0.0/coverageType.xml";

    private static String DEFAULT_GEOM_URL = "http://schemas.opengis.net/wcts/0.0.0/geometryType.xml";

    /**
     * Creates a wcts 0.4.0 capabilities document with the rootnode set to wcts:Capabilities.
     */
    public void createEmptyDocument() {
        setRootElement( XMLTools.create().createElementNS( CommonNamespaces.WCTSNS.toASCIIString(),
                                                           PRE + "Capabilities" ) );
    }

    /**
     * @return the OGCCapabilities parsed from the root node.
     * @throws InvalidCapabilitiesException
     */
    public OGCCapabilities parseCapabilities()
                                              throws InvalidCapabilitiesException {
        WCTSCapabilities caps = null;
        try {
            caps = new WCTSCapabilities( parseVersion(),
                                         parseUpdateSequence(),
                                         parseServiceIdentification(),
                                         parseServiceProvider(),
                                         parseOperationsMetadata(),
                                         parseContents() );
        } catch ( XMLParsingException e ) {
            LOG.logError( e.getMessage(), e );
            throw new InvalidCapabilitiesException( e.getMessage(), ExceptionCode.INVALID_FORMAT );
        } catch ( UnknownCRSException ucrse ) {
            LOG.logError( ucrse.getMessage(), ucrse );
            throw new InvalidCapabilitiesException( ucrse.getMessage(), ExceptionCode.INVALID_FORMAT );
        }
        return caps;
    }

    /**
     * @return the mandatory version string.
     * @throws InvalidCapabilitiesException
     *             with code INVALIDPARAMETERVALUE, if the attribute was not given.
     */
    public String parseVersion()
                                throws InvalidCapabilitiesException {
        Element root = getRootElement();
        String result = new String();
        if ( root != null ) {
            result = root.getAttribute( "version" );
            if ( result == null || "".equals( result.trim() ) ) {
                throw new InvalidCapabilitiesException( Messages.getMessage( "WCTS_ILLEGAL_VERSION", WCTService.version ),
                                                        ExceptionCode.INVALIDPARAMETERVALUE );
            }
        }

        if ( !WCTService.version.equalsIgnoreCase( result ) ) {
            throw new InvalidCapabilitiesException( Messages.getMessage( "WCTS_ILLEGAL_VERSION", WCTService.version ),
                                                    ExceptionCode.INVALIDPARAMETERVALUE );
        }
        return result;

    }

    /**
     * @return the optional updateSeqequence String.
     */
    public String parseUpdateSequence() {
        Element root = getRootElement();
        String result = new String();
        if ( root != null ) {
            result = root.getAttribute( "updateSequence" );
        }
        return result;
    }

    /**
     * Parses the optional wcts:Content element of the wcts:Capabilities element.
     * 
     * @return the content bean representation
     * @throws XMLParsingException
     * @throws UnknownCRSException
     */
    protected Content parseContents()
                                     throws XMLParsingException, UnknownCRSException {
        Element contents = getElement( getRootElement(), PRE + "Contents", nsContext );
        if ( contents == null ) {
            return null;
        }
        List<String> transformations = getNodesAsStringList( contents, PRE + "Transformation", nsContext );
        List<String> methods = getNodesAsStringList( contents, PRE + "Method", nsContext );
        List<String> sCRSs = getNodesAsStringList( contents, PRE + "SourceCRS", nsContext );
        if ( sCRSs == null || sCRSs.size() == 0 ) {
            throw new XMLParsingException( "The " + PRE
                                           + "Content node of the "
                                           + PRE
                                           + "Capabilites must have at least one SourceCRS element." );
        }
        List<CoordinateSystem> sourceCRSs = new ArrayList<CoordinateSystem>( sCRSs.size() );
        for ( String crs : sCRSs ) {
            sourceCRSs.add( CRSFactory.create( crs ) );
        }

        List<String> tCRSs = getNodesAsStringList( contents, PRE + "TargetCRS", nsContext );
        if ( tCRSs == null || tCRSs.size() == 0 ) {
            throw new XMLParsingException( "The " + PRE
                                           + "Content node of the "
                                           + PRE
                                           + "Capabilites must have at least one TargetCRS element." );
        }

        List<CoordinateSystem> targetCRSs = new ArrayList<CoordinateSystem>( tCRSs.size() );
        for ( String crs : tCRSs ) {
            targetCRSs.add( CRSFactory.create( crs ) );
        }
        CoverageAbilities coverageAbilities = parseCoverageAbilities( getElement( contents,
                                                                                  PRE + "CoverageAbilities",
                                                                                  nsContext ) );
        FeatureAbilities featureAbilities = parseFeatureAbilities( getElement( contents,
                                                                               PRE + "FeatureAbilities",
                                                                               nsContext ) );
        if ( coverageAbilities == null && featureAbilities == null ) {
            throw new XMLParsingException( "Both the " + PRE
                                           + "CoverageAbilities and "
                                           + PRE
                                           + "FeatureAbilities elements are null, at least one must be present in the "
                                           + PRE
                                           + "Content element of the WCTS-capabilities document." );
        }

        List<Pair<String, String>> metadata = parseMetadatas( getElements( contents, PRE_OWS + "Metadata", nsContext ) );

        boolean userDefinedCRS = getRequiredNodeAsBoolean( contents, "@userDefinedCRSs", nsContext );

        return new Content( transformations,
                            methods,
                            sourceCRSs,
                            targetCRSs,
                            coverageAbilities,
                            featureAbilities,
                            metadata,
                            userDefinedCRS );
    }

    /**
     * @param element
     * @return the FeatureAbilities
     * @throws XMLParsingException
     */
    private FeatureAbilities parseFeatureAbilities( Element element )
                                                                     throws XMLParsingException {
        if ( element == null ) {
            return null;
        }
        List<Element> requiredElements = getRequiredElements( element, PRE + "GeometryType", nsContext );
        List<Pair<String, String>> geometryTypes = new ArrayList<Pair<String, String>>( requiredElements.size() );
        for ( Element geomElement : requiredElements ) {
            Pair<String, String> t = parseCodeType( geomElement, DEFAULT_GEOM_URL );
            if ( t != null ) {
                geometryTypes.add( t );
            }
        }

        requiredElements = getRequiredElements( element, PRE + "FeatureFormat", nsContext );
        List<InputOutputFormat> featureFormats = new ArrayList<InputOutputFormat>( requiredElements.size() );
        for ( Element featureFormElement : requiredElements ) {
            InputOutputFormat t = parseInputOutputFormatType( featureFormElement );
            if ( t != null ) {
                featureFormats.add( t );
            }
        }

        boolean remoteProperties = getRequiredNodeAsBoolean( element, "@remoteProperties", nsContext );

        return new FeatureAbilities( geometryTypes, featureFormats, remoteProperties );
    }

    /**
     * Parses given element for coverageType, CoverageFormat and InterpolatinMethods, the latter is not evaluated yet
     * though.
     * 
     * @param caElement
     * @return the CoverageAbilities
     * @throws XMLParsingException
     */
    private CoverageAbilities parseCoverageAbilities( Element caElement )
                                                                         throws XMLParsingException {
        if ( caElement == null ) {
            return null;
        }
        List<Element> ctElements = getRequiredElements( caElement, PRE + "CoverageType", nsContext );
        List<Pair<String, String>> coverageTypes = new ArrayList<Pair<String, String>>( ctElements.size() );
        for ( Element ct : ctElements ) {
            Pair<String, String> t = parseCodeType( ct, DEFAULT_COV_URL );
            if ( t != null ) {
                coverageTypes.add( t );
            }
        }
        ctElements = getRequiredElements( caElement, PRE + "CoverageFormat", nsContext );
        List<InputOutputFormat> coverageFormats = new ArrayList<InputOutputFormat>( ctElements.size() );
        for ( Element ct : ctElements ) {
            InputOutputFormat t = parseInputOutputFormatType( ct );
            if ( t != null ) {
                coverageFormats.add( t );
            }
        }
        List<Element> interPolationMethods = getRequiredElements( caElement,
                                                                  WCS_1_2_0_PREFIX + ":InterpolationMethods",
                                                                  nsContext );
        LOG.logWarning( "The " + WCS_1_2_0_PREFIX + ":InterpolationMethods are not evaluated yet." );
        return new CoverageAbilities( coverageTypes, coverageFormats, interPolationMethods );
    }

    /**
     * return the &lt;value,codetype &gt; pair of a CodeType element.
     * 
     * @param elem
     *            to parse from
     * @param defaultString
     *            the String to use as a default value.
     * @return the &lt;text(), codeType-attribute&gt; pair or <code>null</code> if the element is <code>null</code>
     */
    private Pair<String, String> parseCodeType( Element elem, String defaultString ) {
        String value = getStringValue( elem ) ;
        String attrib = elem.getAttribute( "codeSpace" );
        if ( attrib == null || "".equals( attrib ) ) {
            attrib = defaultString;
        }
        return new Pair<String, String>( value, attrib );
    }

    /**
     * return the <mimetype,<input,output>> pair of a CodeType element.
     * 
     * @param elem
     *            to parse from
     * @return the <text(), <input,output>> pair or <code>null</code> if the element is <code>null</code>
     * @throws XMLParsingException
     */
    private InputOutputFormat parseInputOutputFormatType( Element elem )
                                                                        throws XMLParsingException {
        String value = elem.getTextContent();
        boolean input = getNodeAsBoolean( elem, "@input", nsContext, true );
        boolean output = getNodeAsBoolean( elem, "@output", nsContext, true );
        return new InputOutputFormat( value, input, output );
    }

}
