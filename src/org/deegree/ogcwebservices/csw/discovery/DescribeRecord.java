//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/discovery/DescribeRecord.java $
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

package org.deegree.ogcwebservices.csw.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.MissingParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.csw.AbstractCSWRequest;
import org.deegree.ogcwebservices.csw.CSWPropertiesAccess;
import org.w3c.dom.Element;

/**
 * The mandatory DescribeRecord operation allows a client to discover elements of the information
 * model supported by the target catalogue service. The operation allows some or all of the
 * information model to be described.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9348 $, $Date: 2007-12-27 17:59:14 +0100 (Thu, 27 Dec 2007) $
 */
public class DescribeRecord extends AbstractCSWRequest {

    private static final long serialVersionUID = 6554937884331546780L;

    private static final ILogger LOG = LoggerFactory.getLogger( DescribeRecord.class );

    private static NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    private Map namespaceMappings;

    private String[] typeNames;

    private String outputFormat;

    private URI schemaLanguage;

    /**
     * creates a GetRecords request from the XML fragment passed. The passed element must be valid
     * against the OGC CSW 2.0 GetRecords schema.
     * 
     * @param id
     *            unique ID of the request
     * @param root
     *            root element of the GetRecors request
     * @return
     */
    public static DescribeRecord create( String id, Element root )
                            throws MissingParameterValueException, InvalidParameterValueException,
                            OGCWebServiceException {

        String version = null;
        try {
            // first try to read verdsion attribute which is optional for CSW 2.0.0 and 2.0.1
            version = XMLTools.getNodeAsString( root, "./@version", nsContext, null );
        } catch ( XMLParsingException e ) {

        }
        if ( version == null ) {
            // if no version attribute has been set try mapping namespace URI to a version;
            // this is not well defined for 2.0.0 and 2.0.1 which uses the same namespace.
            // in this case 2.0.0 will be returned!
            version = CSWPropertiesAccess.getString( root.getNamespaceURI() );
        }

        // read class for version depenging parsing of DescribeRecord request from properties
        String className = CSWPropertiesAccess.getString( "DescribeRecord" + version );
        Class clzz = null;
        try {
            clzz = Class.forName( className );
        } catch ( ClassNotFoundException e ) {
            LOG.logError( e.getMessage(), e );
            throw new InvalidParameterValueException( e.getMessage(), e );
        }
        DescribeRecordDocument document = null;
        try {
            document = (DescribeRecordDocument) clzz.newInstance();
        } catch ( InstantiationException e ) {
            LOG.logError( e.getMessage(), e );
            throw new InvalidParameterValueException( e.getMessage(), e );
        } catch ( IllegalAccessException e ) {
            LOG.logError( e.getMessage(), e );
            throw new InvalidParameterValueException( e.getMessage(), e );
        }

        document.setRootElement( root );
        return document.parse( id );

    }

    /**
     * Creates a new <code>DecribeRecord</code> instance from the values stored in the submitted
     * Map. Keys (parameter names) in the Map must be uppercase.
     * 
     * @TODO evaluate vendorSpecificParameter
     * 
     * @param kvp
     *            Map containing the parameters
     * @exception InvalidParameterValueException
     * @throws MissingParameterValueException
     */
    public static DescribeRecord create( Map<String, String> kvp )
                            throws InvalidParameterValueException, MissingParameterValueException {
        

        String id;
        String version;
        Map<String, String> vendorSpecificParameter = new HashMap<String, String>();
        Map namespaceMappings;
        String[] typeNames = new String[0];
        String outputFormat;
        URI schemaLanguage;

        // 'ID'-attribute (optional)
        id = getParam( "ID", kvp, "" );

        // 'VERSION'-attribute (mandatory)
        version = getRequiredParam( "VERSION", kvp );

        // 'NAMESPACE'-attribute (optional)
        namespaceMappings = getNSMappings( getParam( "NAMESPACE", kvp, null ) );

        // 'TYPENAME'-attribute (optional)
        String typeNamesString = getParam( "TYPENAME", kvp, null );
        if ( typeNamesString != null ) {
            typeNames = typeNamesString.split( "," );
        }

        // 'OUTPUTFORMAT'-attribute (optional)
        if ( "2.0.2".equals( version  )) {
            outputFormat = getParam( "OUTPUTFORMAT", kvp, "application/xml" );
        } else {
            outputFormat = getParam( "OUTPUTFORMAT", kvp, "text/xml" );
        }

        // 'SCHEMALANGUAGE'-attribute (optional)
        String schemaLanguageString = getParam( "SCHEMALANGUAGE", kvp, "XMLSCHEMA" );
        try {
            schemaLanguage = new URI( schemaLanguageString );
        } catch ( URISyntaxException e ) {
            String msg = "Value '" + schemaLanguageString
                         + "' for parameter 'SCHEMALANGUAGE' is invalid. Must denote a valid URI.";
            throw new InvalidParameterValueException( msg );
        }

        
        return new DescribeRecord( id, version, vendorSpecificParameter, namespaceMappings, typeNames, outputFormat,
                                   schemaLanguage );
    }

    /**
     * Creates a new <code>DescribeRecord</code> instance.
     * 
     * @param id
     * @param version
     * @param vendorSpecificParameter
     */
    DescribeRecord( String id, String version, Map<String, String> vendorSpecificParameter ) {
        super( version, id, vendorSpecificParameter );
    }

    /**
     * Creates a new <code>DescribeRecord</code> instance.
     * 
     * @param id
     * @param version
     * @param vendorSpecificParameter
     * @param namespaceMappings
     * @param typeNames
     * @param outputFormat
     * @param schemaLanguage
     */
    DescribeRecord( String id, String version, Map<String, String> vendorSpecificParameter, Map namespaceMappings,
                    String[] typeNames, String outputFormat, URI schemaLanguage ) {
        this( id, version, vendorSpecificParameter );
        this.namespaceMappings = namespaceMappings;
        this.typeNames = typeNames;
        this.outputFormat = outputFormat;
        this.schemaLanguage = schemaLanguage;
    }

    /**
     * Used to specify namespace(s) and their prefix(es). Format is [prefix:]uri. If prefix is not
     * specified, then this is the default namespace.
     * <p>
     * Zero or one (Optional). Include value for each namespace used by a TypeName. If not included,
     * all qualified names are in the default namespace
     */
    public Map getNamespaces() {
        return this.namespaceMappings;
    }

    /**
     * One or more qualified type names to be described.
     * <p>
     * Zero or one (Optional). Default action is to describe all types known to server.
     * 
     */
    public String[] getTypeNames() {
        return this.typeNames;
    }

    /**
     * A MIME type indicating the format that the output document should have.
     * <p>
     * Zero or one (Optional). Default value is text/xml
     * 
     */
    public String getOutputFormat() {
        return this.outputFormat;
    }

    /**
     * Default value is 'XMLSCHEMA'.
     * 
     */
    public URI getSchemaLanguage() {
        return this.schemaLanguage;
    }
}
