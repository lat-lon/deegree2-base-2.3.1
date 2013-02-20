//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/operation/AbstractWFSRequest.java $
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
package org.deegree.ogcwebservices.wfs.operation;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.model.filterencoding.AbstractFilter;
import org.deegree.model.filterencoding.ComplexFilter;
import org.deegree.model.filterencoding.Filter;
import org.deegree.ogcwebservices.AbstractOGCWebServiceRequest;
import org.deegree.ogcwebservices.InconsistentRequestException;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.wfs.WFService;
import org.w3c.dom.Document;

/**
 * Abstract base class for requests to web feature services.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 */
public class AbstractWFSRequest extends AbstractOGCWebServiceRequest {

    private static final ILogger LOG = LoggerFactory.getLogger( AbstractWFSRequest.class );

    private static final long serialVersionUID = 6691114984307038750L;

    /** GML2 format * */
    public static String FORMAT_GML2 = "text/xml; subtype=gml/2.1.2";

    /** GML2 format (WFS 1.00 style) * */
    public static String FORMAT_GML2_WFS100 = "GML2";

    /** GML3 format * */
    public static String FORMAT_GML3 = "text/xml; subtype=gml/3.1.1";

    /** Generic XML format * */
    public static String FORMAT_XML = "XML";

    private String handle = null;

    /**
     * Creates a new <code>AbstractWFSRequest</code> instance.
     * 
     * @param version
     * @param id
     * @param handle
     * @param vendorSpecificParameter
     */
    protected AbstractWFSRequest( String version, String id, String handle, Map<String, String> vendorSpecificParameter ) {
        super( version, id, vendorSpecificParameter );
        this.handle = handle;
    }

    /**
     * Returns the value of the service attribute (WFS).
     * 
     * @return the value of the service attribute (WFS)
     */
    public String getServiceName() {
        return "WFS";
    }

    /**
     * Returns the value of the handle attribute.
     * <p>
     * The purpose of the <b>handle</b> attribute is to allow a client application to associate a
     * mnemonic name with a request for error handling purposes. If a <b>handle</b> is specified,
     * and an exception is encountered, a Web Feature Service may use the <b>handle</b> to identify
     * the offending element.
     * 
     * @return the value of the handle attribute
     */
    public String getHandle() {
        return this.handle;
    }

    /**
     * Checks that the "VERSION" parameter value equals a supported version.
     * 
     * @param model
     *            contains the parameters of the request
     * @return value for "VERSION" parameter, never null
     * @throws InconsistentRequestException
     *             if parameter is not present
     * @throws InvalidParameterValueException
     */
    protected static String checkVersionParameter( Map<String, String> model )
                            throws InconsistentRequestException, InvalidParameterValueException {
        String version = model.get( "VERSION" );
        if ( version == null ) {
            throw new InconsistentRequestException( "'VERSION' parameter must be set." );
        }
        
        if ( !WFService.VERSION.equals( version ) && !"1.0.0".equals( version ) ) {
            String msg = Messages.getMessage( "WFS_REQUEST_UNSUPPORTED_VERSION", version,
                                              "1.0.0 and " + WFService.VERSION );
            throw new InvalidParameterValueException( msg );
        }
        return version;
    }

    /**
     * Checks that the "SERVICE" parameter value equals the name of the service.
     * 
     * TODO move this to AbstractOGCWebServiceRequest
     * 
     * @param model
     *            contains the parameters of the request
     * @throws InconsistentRequestException
     *             if parameter is not present or does not the service name
     */
    protected static void checkServiceParameter( Map<String, String> model )
                            throws InconsistentRequestException {
        String service = model.get( "SERVICE" );
        if ( !"WFS".equals( service ) ) {
            throw new InconsistentRequestException( "'SERVICE' parameter must be 'WFS', but is '" + service + "'." );
        }
    }

    /**
     * Extracts the qualified type names from the TYPENAME parameter.
     * 
     * @param kvp
     * @return qualified type names (empty array if TYPENAME parameter is not present)
     * @throws InvalidParameterValueException
     */
    protected static QualifiedName[] extractTypeNames( Map<String, String> kvp )
                            throws InvalidParameterValueException {
        QualifiedName[] typeNames = new QualifiedName[0];
        NamespaceContext nsContext = extractNamespaceParameter( kvp );
        String typeNameString = kvp.get( "TYPENAME" );
        if ( typeNameString != null ) {
            String[] typeNameStrings = typeNameString.split( "," );
            typeNames = new QualifiedName[typeNameStrings.length];
            for ( int i = 0; i < typeNameStrings.length; i++ ) {
                typeNames[i] = transformToQualifiedName( typeNameStrings[i], nsContext );
            }
        }
        return typeNames;
    }

    /**
     * Extracts the namespace bindings from the NAMESPACE parameter.
     * <p>
     * Example:
     * <ul>
     * <li><code>NAMESPACE=xmlns(myns=http://www.someserver.com),xmlns(yourns=http://www.someotherserver.com)</code></li>
     * </ul>
     * <p>
     * The default namespace may also be bound (two variants are supported):
     * <ul>
     * <li><code>NAMESPACE=xmlns(=http://www.someserver.com)</code></li>
     * <li><code>NAMESPACE=xmlns(http://www.someserver.com)</code></li>
     * </ul>
     * 
     * @param model
     *            the parameters of the request
     * @return namespace context
     * @throws InvalidParameterValueException
     */
    protected static NamespaceContext extractNamespaceParameter( Map<String, String> model )
                            throws InvalidParameterValueException {

        String nsString = model.get( "NAMESPACE" );

        NamespaceContext nsContext = new NamespaceContext();
        if ( nsString != null ) {
            String nsDecls[] = nsString.split( "," );
            for ( int i = 0; i < nsDecls.length; i++ ) {
                String nsDecl = nsDecls[i];
                if ( nsDecl.startsWith( "xmlns(" ) && nsDecl.endsWith( ")" ) ) {
                    nsDecl = nsDecl.substring( 6, nsDecl.length() - 1 );
                    int assignIdx = nsDecl.indexOf( '=' );
                    String prefix = "";
                    String nsURIString = null;
                    if ( assignIdx != -1 ) {
                        prefix = nsDecl.substring( 0, assignIdx );
                        nsURIString = nsDecl.substring( assignIdx + 1 );
                    } else {
                        nsURIString = nsDecl;
                    }
                    try {
                        URI nsURI = new URI( nsURIString );
                        nsContext.addNamespace( prefix, nsURI );
                    } catch ( URISyntaxException e ) {
                        String msg = Messages.getMessage( "WFS_NAMESPACE_PARAM_INVALID_URI", nsURIString, prefix );
                        throw new InvalidParameterValueException( msg );
                    }
                } else {
                    String msg = Messages.getMessage( "WFS_NAMESPACE_PARAM" );
                    throw new InvalidParameterValueException( msg );
                }
            }
        }
        return nsContext;
    }

    /**
     * Extracts a <code>Filter</code> from the BBOX parameter.
     * 
     * TODO handle other dimension count and crs
     * 
     * @param model
     * @return filter representing the BBOX parameter (null, if no BBOX parameter specified)
     * @throws InvalidParameterValueException
     */
    protected static Filter extractBBOXFilter( Map<String, String> model )
                            throws InvalidParameterValueException {

        ComplexFilter filter = null;
        String bboxString = model.get( "BBOX" );
        if ( bboxString != null ) {
            String msg = "Parameter 'BBOX' is currently not supported. Please use the 'FILTER' parameter instead.";
            throw new InvalidParameterValueException( msg );
            // String[] parts = bboxString.split( "," );
            // double[] coords = new double[4];
            //
            // if ( parts.length > 5 ) {
            // String msg = Messages.getString( "WFS_BBOX_PARAM_WRONG_COORD_COUNT" );
            // throw new InvalidParameterValueException( msg );
            // }
            //
            // for ( int i = 0; i < coords.length; i++ ) {
            // try {
            // coords[i] = Double.parseDouble( parts[i] );
            // } catch ( NumberFormatException e ) {
            // String msg = Messages.getMessage( "WFS_BBOX_PARAM_COORD_INVALID", coords[i] );
            // throw new InvalidParameterValueException( msg );
            // }
            // }
            //
            // // build filter
            // Envelope bbox = GeometryFactory.createEnvelope( coords[0], coords[1], coords[2],
            // coords[3], null );
            // Surface surface;
            // try {
            // surface = GeometryFactory.createSurface( bbox, null );
            // } catch ( GeometryException e ) {
            // String msg = Messages.getMessage( "WFS_BBOX_PARAM_BBOX_INVALID", e.getMessage() );
            // throw new InvalidParameterValueException( msg );
            // }
            // Operation op = new SpatialOperation( OperationDefines.BBOX, null, surface );
            // filter = new ComplexFilter( op );
        }
        return filter;
    }

    /**
     * Extracts the FILTER parameter and assigns them to the requested type names.
     * <p>
     * This is necessary, because it is allowed to specify a filter for each requested feature type.
     * 
     * @param kvp
     * @param typeNames
     * @return map with the assignments of type names to filters
     * @throws InvalidParameterValueException
     */
    protected static Map<QualifiedName, Filter> extractFilters( Map<String, String> kvp, QualifiedName[] typeNames )
                            throws InvalidParameterValueException {
        Map<QualifiedName, Filter> filterMap = new HashMap<QualifiedName, Filter>();
        String filterString = kvp.get( "FILTER" );
        if ( filterString != null ) {
            String[] filterStrings = filterString.split( "\\)" );
            if ( filterStrings.length != typeNames.length ) {
                String msg = Messages.getMessage( "WFS_FILTER_PARAM_WRONG_COUNT", Integer.toString( filterStrings.length ),
                                                  Integer.toString( typeNames.length ) );
                throw new InvalidParameterValueException( msg );
            }
            for ( int i = 0; i < filterStrings.length; i++ ) {
                // remove possible leading parenthesis
                if ( filterStrings[i].startsWith( "(" ) ) {
                    filterStrings[i] = filterStrings[i].substring( 1 );
                }
                Document doc;
                try {
                    doc = XMLTools.parse( new StringReader( filterStrings[i] ) );
                    Filter filter = AbstractFilter.buildFromDOM( doc.getDocumentElement() );
                    filterMap.put( typeNames[i], filter );
                } catch ( Exception e ) {
                    LOG.logError( e.getMessage(), e );
                    String msg = Messages.getMessage( "WFS_FILTER_PARAM_PARSING", e.getMessage() );
                    throw new InvalidParameterValueException( msg );
                }
            }
        }
        return filterMap;
    }

    /**
     * Transforms a type name to a qualified name using the given namespace bindings.
     * 
     * @param name
     * @param nsContext
     * @return QualifiedName
     * @throws InvalidParameterValueException
     */
    private static QualifiedName transformToQualifiedName( String name, NamespaceContext nsContext )
                            throws InvalidParameterValueException {
        QualifiedName typeName;
        String prefix = "";
        int idx = name.indexOf( ':' );
        if ( idx != -1 ) {
            prefix = name.substring( 0, idx );
            String localName = name.substring( idx + 1 );
            URI nsURI = nsContext.getURI( prefix );
            if ( nsURI == null ) {
                String msg = Messages.getMessage( "WFS_TYPENAME_PARAM_INVALID_URI", prefix );
                throw new InvalidParameterValueException( msg );
            }
            typeName = new QualifiedName( prefix, localName, nsURI );
        } else {
            // default namespace prefix ("")
            URI nsURI = nsContext.getURI( "" );
            typeName = new QualifiedName( name, nsURI );
        }
        return typeName;
    }
}
