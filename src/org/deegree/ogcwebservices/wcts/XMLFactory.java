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

package org.deegree.ogcwebservices.wcts;

import static org.deegree.framework.xml.XMLTools.appendElement;
import static org.deegree.ogcbase.CommonNamespaces.DEEGREEWCTS;
import static org.deegree.ogcbase.CommonNamespaces.DEEGREEWCTS_PREFIX;
import static org.deegree.ogcbase.CommonNamespaces.OWSNS_1_1_0;
import static org.deegree.ogcbase.CommonNamespaces.OWS_1_1_0PREFIX;
import static org.deegree.ogcbase.CommonNamespaces.WCTSNS;
import static org.deegree.ogcbase.CommonNamespaces.WCTS_PREFIX;
import static org.deegree.ogcbase.CommonNamespaces.XLINK_PREFIX;
import static org.deegree.ogcbase.CommonNamespaces.XLNNS;

import java.io.IOException;
import java.util.List;

import javax.vecmath.Point3d;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.Pair;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLTools;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureException;
import org.deegree.model.feature.GMLFeatureAdapter;
import org.deegree.model.spatialschema.GMLGeometryAdapter;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.model.spatialschema.GeometryException;
import org.deegree.ogcwebservices.wcts.capabilities.Content;
import org.deegree.ogcwebservices.wcts.capabilities.CoverageAbilities;
import org.deegree.ogcwebservices.wcts.capabilities.FeatureAbilities;
import org.deegree.ogcwebservices.wcts.capabilities.InputOutputFormat;
import org.deegree.ogcwebservices.wcts.capabilities.WCTSCapabilities;
import org.deegree.ogcwebservices.wcts.data.FeatureCollectionData;
import org.deegree.ogcwebservices.wcts.data.GeometryData;
import org.deegree.ogcwebservices.wcts.data.SimpleData;
import org.deegree.ogcwebservices.wcts.data.TransformableData;
import org.deegree.ogcwebservices.wcts.operation.GetResourceByID;
import org.deegree.ogcwebservices.wcts.operation.Transform;
import org.deegree.ogcwebservices.wcts.operation.TransformResponse;
import org.deegree.owscommon_1_1_0.Manifest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * The <code>XMLFactory</code> provides helper methods to create xml-doc representations of bean
 * encapsulations.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */
public class XMLFactory {

    private static ILogger LOG = LoggerFactory.getLogger( XMLFactory.class );

    private static final String PRE = WCTS_PREFIX + ":";

    /**
     * Exports an GetResourceById bean to xml.
     * 
     * @param resourceByID
     * @return a dom-representation.
     */
    public static XMLFragment create( GetResourceByID resourceByID ) {
        return null;
    }

    /**
     * Exports an WCTSCapabilies bean to xml.
     * 
     * @param capabilities
     *            to be exported.
     * @return an xml-dom-representation of the given bean or <code>null</code> if the given
     *         parameter is <code>null</code>.
     * 
     */
    public static XMLFragment create( WCTSCapabilities capabilities ) {
        if ( capabilities == null ) {
            return null;
        }
        org.deegree.owscommon_1_1_0.XMLFactory fac = new org.deegree.owscommon_1_1_0.XMLFactory();
        Document doc = XMLTools.create();
        Element root = doc.createElementNS( WCTSNS.toASCIIString(), PRE + "Capabilities" );
        XMLFragment result = new XMLFragment( root );
        fac.exportCapabilities( root, capabilities );
        Content content = capabilities.getContents();
        if ( content != null ) {
            appendCapabilitiesContent( root, content );
        }
        return result;
    }

    /**
     * Creates a response to a Transform request. The wcts spec defines it to be a
     * ows_1_1_0:OperationResponse, this method appends the deegreewcts:MultiParts or the
     * deegreewcts:InlineData element(s) to the root node.
     * 
     * @param transformResponse
     *            to create.
     * @return the ows_1_1_0:OperationResponse with deegreewcts:MultiPart element added or
     *         <code>null</code> if the given param is <code>null</code>.
     */
    public static XMLFragment createResponse( TransformResponse transformResponse ) {
        if ( transformResponse == null ) {
            return null;
        }
        org.deegree.owscommon_1_1_0.XMLFactory owsFac = new org.deegree.owscommon_1_1_0.XMLFactory();
        XMLFragment result = null;
        Element dataElement = null;
        switch ( transformResponse.getDataPresentation() ) {
        case Transform.INLINE:
            result = createOperationResponse( owsFac, transformResponse.getInputData() );
            if ( result != null ) {
                dataElement = appendElement( result.getRootElement(), DEEGREEWCTS, DEEGREEWCTS_PREFIX + ":InlineData" );
            }
            break;
        case Transform.MULTIPART: // fall through
        default:
            result = owsFac.createOperationResponse( transformResponse.getInputData() );
            if ( result != null ) {
                dataElement = appendElement( result.getRootElement(), DEEGREEWCTS, DEEGREEWCTS_PREFIX + ":MultiParts" );
            }
            break;
        }
        appendTransformableData( dataElement, transformResponse.getTransformableData() );
        return result;
    }

    /**
     * Will create an XMLFragment which holds the d_wcts:OperationResponse as the root element,
     * values from the given manifest will be appended by the given ows_1_1 XMLFactory.
     * 
     * @param owsFactory
     *            an instance of the ows_1-1 XMLFactory
     * 
     * @param operationResponse
     *            to create the dom-xml representation from.
     * @return the xmlFragment as defined in ows 1.1.0 or <code>null</code> if the given parameter
     *         is <code>null</code>.
     */
    public static XMLFragment createOperationResponse( org.deegree.owscommon_1_1_0.XMLFactory owsFactory,
                                                       Manifest operationResponse ) {
        if ( operationResponse == null ) {
            return null;
        }
        Document doc = XMLTools.create();
        Element root = doc.createElementNS( DEEGREEWCTS.toASCIIString(), PRE + "OperationResponse" );
        owsFactory.appendManifest( root, operationResponse );
        return new XMLFragment( root );
    }

    /**
     * Appends the TransformableData bean, as an xml-dom element to the given root. If either one of
     * the parameters is <code>null</code>, this method just returns.
     * 
     * @param root
     *            to append to.
     * @param transformableData
     *            to append.
     */
    protected static void appendTransformableData( Element root, TransformableData<?> transformableData ) {
        if ( root == null || transformableData == null ) {
            return;
        }
        if ( transformableData instanceof SimpleData ) {
            appendSimpleData( root, (SimpleData) transformableData );
        } else if ( transformableData instanceof GeometryData ) {
            appendGeometryData( root, (GeometryData) transformableData );
        } else if ( transformableData instanceof FeatureCollectionData ) {
            appendFeatureCollectionData( root, (FeatureCollectionData) transformableData );
        }
    }

    /**
     * Appends a dom-xml document element with the name
     * {http://www.deegree.org/wcts}:FeatureCollectionData. It will contain all transformed
     * FeaturCollection as it's children. Or if no FeatureCollections were transformed this element
     * will have no children at all.
     * <p>
     * If either one of the parameters is <code>null</code>, this method just returns.
     * </p>
     * 
     * @param root
     *            to append to.
     * @param transformableData
     *            to append.
     */
    protected static void appendFeatureCollectionData( Element root, FeatureCollectionData transformableData ) {
        if ( root == null || transformableData == null ) {
            return;
        }
        GMLFeatureAdapter ad = new GMLFeatureAdapter();
        Element featureCollectionElement = appendElement( root, DEEGREEWCTS, DEEGREEWCTS_PREFIX
                                                                             + ":FeatureCollectionData" );
        List<FeatureCollection> transformedData = transformableData.getTransformedData();
        if ( transformedData != null && transformedData.size() >= 0 ) {
            for ( FeatureCollection featureCollection : transformedData ) {
                if ( featureCollection != null ) {
                    try {
                        ad.append( featureCollectionElement, featureCollection );
                    } catch ( FeatureException e ) {
                        LOG.logError( e.getMessage(), e );
                    } catch ( IOException e ) {
                        LOG.logError( e.getMessage(), e );
                    } catch ( SAXException e ) {
                        LOG.logError( e.getMessage(), e );
                    }
                }
            }
        }
    }

    /**
     * Appends a dom-xml document element with the name {http://www.deegree.org/wcts}:GeometryData.
     * It it will contain all transformed Geometries as it's children. Or if no Geometries were
     * transformed this element will have no children at all.
     * <p>
     * If either one of the parameters is <code>null</code>, this method just returns.
     * </p>
     * 
     * @param root
     *            to append to.
     * @param transformableData
     *            to append.
     */
    protected static void appendGeometryData( Element root, GeometryData transformableData ) {
        if ( root == null || transformableData == null ) {
            return;
        }
        Element geometryElement = appendElement( root, DEEGREEWCTS, DEEGREEWCTS_PREFIX + ":GeometryData" );
        Document doc = geometryElement.getOwnerDocument();
        List<Geometry> transformedGeometries = transformableData.getTransformedData();
        if ( transformedGeometries.size() >= 0 ) {
            for ( Geometry geom : transformedGeometries ) {
                if ( geom != null ) {
                    try {
                        StringBuffer sb = GMLGeometryAdapter.export( geom );
                        Element tmp = XMLTools.getStringFragmentAsElement( sb.toString() );
                        if ( tmp != null ) {
                            tmp = (Element) doc.importNode( tmp, true );
                            geometryElement.appendChild( tmp );
                        }
                    } catch ( GeometryException e ) {
                        LOG.logError( e.getMessage(), e );
                    } catch ( SAXException e ) {
                        LOG.logError( e.getMessage(), e );
                    } catch ( IOException e ) {
                        LOG.logError( e.getMessage(), e );
                    }
                }
            }
        }
    }

    /**
     * Appends a dom-xml document element with the name is {http://www.deegree.org/wcts}:SimpleData.
     * It will contain the points as a separated list as defined by the 'cs' separator. The element
     * has the attribute 'srsDimension'. The list elements can therefore be interpreted as a tuple
     * of the value of 'srsDimension'. If no points were transformed the list will be empty.
     * <p>
     * If either one of the parameters is <code>null</code>, this method just returns.
     * </p>
     * 
     * @param root
     *            to append to.
     * @param transformableData
     *            to append.
     */
    protected static void appendSimpleData( Element root, SimpleData transformableData ) {
        if ( root == null || transformableData == null ) {
            return;
        }
        Element simpleDataElement = appendElement( root, DEEGREEWCTS, DEEGREEWCTS_PREFIX + ":SimpleData" );
        int dim = transformableData.getTargetCRS().getDimension();
        final String ts = transformableData.getTupleSeparator();
        simpleDataElement.setAttribute( "ts", ts );
        final String cs = transformableData.getCoordinateSeparator();
        simpleDataElement.setAttribute( "cs", cs );
        List<Point3d> transformedPoints = transformableData.getTransformedData();
        StringBuilder sb = new StringBuilder( transformedPoints.size() * dim );
        for ( int i = 0; i < transformedPoints.size(); ++i ) {
            Point3d point = transformedPoints.get( i );
            if ( point != null ) {
                sb.append( point.x );
                sb.append( cs );
                sb.append( point.y );
                if ( dim == 3 ) {
                    sb.append( cs );
                    sb.append( point.z );
                }
                if ( ( i + 1 ) < transformedPoints.size() ) {
                    sb.append( ts );
                }
            }
        }
        XMLTools.setNodeValue( simpleDataElement, sb.toString() );
    }

    /**
     * Appends the WCTSContent bean, as an xml-dom element to the given root. If either one of the
     * parameters is <code>null</code>, this method just returns.
     * 
     * @param root
     *            to append the values to.
     * @param content
     *            to be appended.
     */
    protected static void appendCapabilitiesContent( Element root, Content content ) {
        if ( content == null || root == null ) {
            return;
        }
        Element contentElement = appendElement( root, WCTSNS, PRE + "Contents" );
        List<String> transformations = content.getTransformations();
        if ( transformations != null && transformations.size() > 0 ) {
            for ( String transform : transformations ) {
                if ( transform != null ) {
                    appendElement( contentElement, WCTSNS, PRE + "Transformation", transform );
                }
            }
        }

        List<String> methods = content.getMethods();
        if ( methods != null && methods.size() > 0 ) {
            for ( String s : methods ) {
                if ( s != null ) {
                    appendElement( contentElement, WCTSNS, PRE + "Method", s );
                }
            }
        }

        List<CoordinateSystem> sourceCRSs = content.getSourceCRSs();
        if ( sourceCRSs != null && sourceCRSs.size() > 0 ) {
            for ( CoordinateSystem s : sourceCRSs ) {
                if ( s != null ) {
                    appendElement( contentElement, WCTSNS, PRE + "SourceCRS", s.getIdentifier() );
                }
            }
        }

        List<CoordinateSystem> targetCRSs = content.getTargetCRSs();
        if ( targetCRSs != null && targetCRSs.size() > 0 ) {
            for ( CoordinateSystem s : targetCRSs ) {
                if ( s != null ) {
                    appendElement( contentElement, WCTSNS, PRE + "TargetCRS", s.getIdentifier() );
                }
            }
        }

        CoverageAbilities cAbilities = content.getCoverageAbilities();
        if ( cAbilities != null ) {
            Element caElement = appendElement( contentElement, WCTSNS, PRE + "CoverageAbilities" );
            List<Pair<String, String>> coverageTypes = cAbilities.getCoverageTypes();
            if ( coverageTypes != null && coverageTypes.size() > 0 ) {
                for ( Pair<String, String> values : coverageTypes ) {
                    if ( values != null ) {
                        Element ctElement = appendElement( caElement, WCTSNS, PRE + "CoverageType", values.first );
                        ctElement.setAttribute( "codeSpace", values.second );
                    }
                }
            }
            List<InputOutputFormat> cFormats = cAbilities.getCoverageFormats();
            if ( cFormats != null && cFormats.size() > 0 ) {
                for ( InputOutputFormat iof : cFormats ) {
                    appendInputOutput( caElement, iof, "CoverageFormat" );
                }
            }
            List<Element> interpolationMethods = cAbilities.getInterPolationMethods();
            if ( interpolationMethods != null && interpolationMethods.size() > 0 ) {
                for ( Element element : interpolationMethods ) {
                    if ( element != null ) {
                        Element copyOf = (Element) caElement.getOwnerDocument().importNode( element, true );
                        caElement.appendChild( copyOf );
                    }
                }
            }

        }
        FeatureAbilities fAbilities = content.getFeatureAbilities();
        if ( fAbilities != null ) {
            Element faElement = appendElement( contentElement, WCTSNS, PRE + "FeatureAbilities" );
            List<Pair<String, String>> geometryTypes = fAbilities.getGeometryTypes();
            if ( geometryTypes != null && geometryTypes.size() > 0 ) {
                for ( Pair<String, String> values : geometryTypes ) {
                    if ( values != null ) {
                        Element ctElement = appendElement( faElement, WCTSNS, PRE + "GeometryType", values.first );
                        ctElement.setAttribute( "codeSpace", values.second );
                    }
                }
            }
            List<InputOutputFormat> fFormats = fAbilities.getFeatureFormats();
            if ( fFormats != null && fFormats.size() > 0 ) {
                for ( InputOutputFormat iof : fFormats ) {
                    appendInputOutput( faElement, iof, "FeatureFormat" );
                }
            }
            faElement.setAttribute( "remoteProperties", fAbilities.getRemoteProperties() ? "true" : "false" );
        }

        List<Pair<String, String>> metadataList = content.getMetadata();
        if ( metadataList != null && metadataList.size() > 0 ) {
            for ( Pair<String, String> values : metadataList ) {
                if ( values != null ) {
                    Element mdElement = appendElement( contentElement, OWSNS_1_1_0, OWS_1_1_0PREFIX + ":Metadata" );
                    mdElement.setAttributeNS( XLNNS.toASCIIString(), XLINK_PREFIX + ":href", values.first );
                    mdElement.setAttribute( "about", values.second );
                }
            }
        }
        contentElement.setAttribute( "userDefinedCRSs", content.supportsUserDefinedCRS() ? "true" : "false" );
    }

    /**
     * Appends the input output element
     * 
     * @param root
     *            to append to
     * @param inputOutput
     *            to append
     * @param nodeName
     *            the element name, i.e. CoverageFormat or FeatureFormat
     */
    private static void appendInputOutput( Element root, InputOutputFormat inputOutput, String nodeName ) {
        if ( inputOutput != null && root != null ) {
            Element cfElement = appendElement( root, WCTSNS, PRE + nodeName, inputOutput.getValue() );
            cfElement.setAttribute( "input", inputOutput.canInput() ? "true" : "false" );
            cfElement.setAttribute( "output", inputOutput.canOutput() ? "true" : "false" );

        }
    }
}
