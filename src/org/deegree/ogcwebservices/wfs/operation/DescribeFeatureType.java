//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/operation/DescribeFeatureType.java $
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
package org.deegree.ogcwebservices.wfs.operation;

import java.util.Map;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.util.IDGenerator;
import org.deegree.framework.util.KVP2Map;
import org.deegree.ogcwebservices.InconsistentRequestException;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.w3c.dom.Element;

/**
 * Represents a <code>DescribeFeatureType</code> request to a web feature service.
 * <p>
 * The function of the DescribeFeatureType interface is to provide a client the means to request a
 * schema definition of any feature type that a particular WFS can service. The description that is
 * generated will define how a WFS expects a client application to express the state of a feature
 * to be created or the new state of a feature to be updated. The result of a DescribeFeatureType
 * request is an XML document, describing one or more feature types serviced by the WFS.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 */
public class DescribeFeatureType extends AbstractWFSRequest {

    private static final long serialVersionUID = 4403179045869238426L;

    private String outputFormat;

    private QualifiedName[] typeNames;

    /**
     * Creates a new <code>DescribeFeatureType</code> instance.
     * 
     * @param version
     * @param id
     * @param handle
     * @param outputFormat
     * @param typeNames
     */
    DescribeFeatureType( String version, String id, String handle, String outputFormat,
                        QualifiedName[] typeNames, Map<String,String> vendorspecificParameter ) {
        super( version, id, handle, vendorspecificParameter );
        this.outputFormat = outputFormat;
        this.typeNames = typeNames;
    }

    /**
     * Creates a <code>DescribeFeatureType</code> instance from a document that contains the DOM
     * representation of the request.
     * 
     * @param id
     * @param root element that contains the DOM representation of the request
     * @return DescribeFeatureType instance
     * @throws OGCWebServiceException
     */
    public static DescribeFeatureType create( String id, Element root )
                            throws OGCWebServiceException {
        DescribeFeatureTypeDocument doc = new DescribeFeatureTypeDocument();
        doc.setRootElement( root );
        DescribeFeatureType request;
        try {
            request = doc.parse( id );
        } catch ( Exception e ) {
            throw new OGCWebServiceException( "DescribeFeatureType", e.getMessage() );
        }
        return request;
    }

    /**
     * Creates a new <code>DescribeFeatureType</code> instance from the given key-value pair encoded
     * request.
     * 
     * @param id request identifier
     * @param request
     * @return new <code>DescribeFeatureType</code> request
     * @throws InvalidParameterValueException 
     * @throws InconsistentRequestException 
     */
    public static DescribeFeatureType create( String id, String request )
                            throws InconsistentRequestException, InvalidParameterValueException {
        Map<String, String> map = KVP2Map.toMap( request );
        map.put( "ID", id );
        return create( map );
    }

    /**
     * Creates a new <code>DescribeFeatureType</code> request from the given map.
     * 
     * @param request
     * @return new <code>DescribeFeatureType</code> request
     * @throws InconsistentRequestException
     * @throws InvalidParameterValueException
     */
    public static DescribeFeatureType create( Map<String, String> request )
                            throws InconsistentRequestException, InvalidParameterValueException {

        checkServiceParameter( request );
        String version = checkVersionParameter( request );
        String outputFormat = getParam( "OUTPUTFORMAT", request, FORMAT_GML3 );
        QualifiedName[] typeNames = extractTypeNames( request );

        long l = IDGenerator.getInstance().generateUniqueID();
        String id = Long.toString( l );
        return new DescribeFeatureType( version, id, null, outputFormat, typeNames, request );
    }

    /**
     * Returns the value of the outputFormat attribute.
     * <p>
     * The outputFormat attribute, is used to indicate the schema description language that should
     * be used to describe a feature schema. The only mandated format is XML-Schema denoted by the
     * XMLSCHEMA element; other vendor specific formats specified in the capabilities document are
     * also possible.
     * 
     * @return the value of the outputFormat attribute.
     */
    public String getOutputFormat() {
        return this.outputFormat;
    }

    /**
     * Returns the names of the feature types for which the schema is requested.
     * <p>
     * @return the names of the feature types for which the schema is requested.
     */
    public QualifiedName[] getTypeNames() {
        return typeNames;
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String ret = this.getClass().getName() + ":\n";
        ret += ( outputFormat + "\n" );
        if ( typeNames != null ) {
            for ( int i = 0; i < typeNames.length; i++ ) {
                ret += ( typeNames[i] + "\n" );
            }
        }
        return ret;
    }
}
