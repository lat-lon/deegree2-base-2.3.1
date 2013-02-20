//$HeadURL: svn+ssh://rbezema@svn.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/XMLFactory.java $
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
package org.deegree.ogcwebservices.wfs;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import org.deegree.datatypes.xlink.SimpleLink;
import org.deegree.enterprise.DeegreeParams;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.model.filterencoding.capabilities.FilterCapabilities;
import org.deegree.model.metadata.iso19115.Keywords;
import org.deegree.model.metadata.iso19115.Linkage;
import org.deegree.model.metadata.iso19115.OnlineResource;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.Position;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.getcapabilities.DCPType;
import org.deegree.ogcwebservices.getcapabilities.HTTP;
import org.deegree.ogcwebservices.getcapabilities.MetadataURL;
import org.deegree.ogcwebservices.getcapabilities.OperationsMetadata;
import org.deegree.ogcwebservices.getcapabilities.Protocol;
import org.deegree.ogcwebservices.getcapabilities.ServiceIdentification;
import org.deegree.ogcwebservices.getcapabilities.ServiceProvider;
import org.deegree.ogcwebservices.wfs.capabilities.FeatureTypeList;
import org.deegree.ogcwebservices.wfs.capabilities.Operation;
import org.deegree.ogcwebservices.wfs.capabilities.WFSCapabilitiesDocument;
import org.deegree.ogcwebservices.wfs.capabilities.WFSFeatureType;
import org.deegree.ogcwebservices.wfs.capabilities.WFSOperationsMetadata;
import org.deegree.ogcwebservices.wfs.configuration.WFSConfiguration;
import org.w3c.dom.Element;

/**
 * Responsible for the generation of XML representations of objects from the WFS context.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */

public class XMLFactory_1_0_0 {

    private static final URI WFSNS = CommonNamespaces.WFSNS;

    private static final String PRE_WFS = CommonNamespaces.WFS_PREFIX + ":";

    private static final ILogger LOG = LoggerFactory.getLogger( XMLFactory_1_0_0.class );

    private static XMLFactory_1_0_0 factory = null;

    /**
     * @return a cached instance of this XMLFactory.
     */
    public static synchronized XMLFactory_1_0_0 getInstance() {
        if ( factory == null ) {
            factory = new XMLFactory_1_0_0();
        }
        return factory;
    }

    /**
     * Exports a <code>WFSCapabilities</code> instance to a <code>WFSCapabilitiesDocument</code>
     * with version 1_0.
     * 
     * @param config
     * @return DOM representation of the <code>WFSCapabilities</code>
     * @throws IOException
     *             if XML template could not be loaded
     */
    public WFSCapabilitiesDocument export( WFSConfiguration config ) {

        WFSCapabilitiesDocument capabilitiesDocument = new WFSCapabilitiesDocument();

        capabilitiesDocument.createEmptyDocument( "1.0.0" );
        Element root = capabilitiesDocument.getRootElement();

        // Find the default online resource
        DeegreeParams deegreeParams = config.getDeegreeParams();
        String defaultOnlineResource = "http://localhost:8080/deegree/services";
        if ( deegreeParams != null ) {
            OnlineResource or = deegreeParams.getDefaultOnlineResource();
            if ( or != null ) {
                Linkage link = or.getLinkage();
                if ( link != null ) {
                    URL uri = link.getHref();
                    if ( uri != null ) {
                        String tmp = uri.toExternalForm();
                        if ( !"".equals( tmp.toString() ) ) {
                            defaultOnlineResource = tmp;
                        }
                    }
                }
            }
        }

        ServiceIdentification serviceIdentification = config.getServiceIdentification();
        ServiceProvider serviceProvider = config.getServiceProvider();
        OperationsMetadata operationsMetadata = config.getOperationsMetadata();
        FeatureTypeList featureTypeList = config.getFeatureTypeList();

        if ( serviceIdentification != null ) {
            String onlineResource = null;
            if ( serviceProvider != null ) {
                SimpleLink resource = serviceProvider.getProviderSite();
                URI online = resource.getHref();
                onlineResource = online.toASCIIString();
            } else {
                onlineResource = defaultOnlineResource;
            }
            appendService( root, serviceIdentification, onlineResource );
        }

        // Add the capabilities element
        if ( operationsMetadata != null ) {
            appendCapability( root, (WFSOperationsMetadata) operationsMetadata, defaultOnlineResource );
        }

        if ( featureTypeList != null ) {
            appendFeatureTypeList( root, featureTypeList );
        }

        FilterCapabilities filterCapabilities = config.getFilterCapabilities();
        if ( filterCapabilities != null ) {
            org.deegree.model.filterencoding.XMLFactory.appendFilterCapabilities100( root, filterCapabilities );
        }
        return capabilitiesDocument;
    }

    /**
     * Appends the DOM representation of the {@link ServiceIdentification} section to the passed
     * {@link Element}.
     * 
     * @param root
     *            to which to append the service section.
     * @param serviceIdentification
     *            bean of the service identification element.
     * @param onlineResource
     *            to be used as a default onlineResource.
     */
    private void appendService( Element root, ServiceIdentification serviceIdentification, String onlineResource ) {

        // 'Service'-element
        Element service = XMLTools.appendElement( root, WFSNS, PRE_WFS + "Service" );

        // 'Name'-element
        String tmpValue = serviceIdentification.getName();
        tmpValue = checkForEmptyValue( tmpValue, PRE_WFS + "Name", "WFS" );
        XMLTools.appendElement( service, WFSNS, PRE_WFS + "Name", tmpValue );

        tmpValue = serviceIdentification.getTitle();
        tmpValue = checkForEmptyValue( tmpValue, PRE_WFS + "Title", "A Web Feature Service" );
        XMLTools.appendElement( service, WFSNS, PRE_WFS + "Title", tmpValue );

        tmpValue = serviceIdentification.getAbstract();
        if ( tmpValue != null && "".equals( tmpValue.trim() ) ) {
            XMLTools.appendElement( service, WFSNS, PRE_WFS + "Abstract", tmpValue );
        }

        Keywords[] keywords = serviceIdentification.getKeywords();
        appendKeyWords( service, keywords );

        XMLTools.appendElement( service, WFSNS, PRE_WFS + "OnlineResource", onlineResource );

        tmpValue = serviceIdentification.getFees();
        if ( tmpValue == null || "".equals( tmpValue.trim() ) ) {
            tmpValue = "NONE";
        }
        XMLTools.appendElement( service, WFSNS, PRE_WFS + "Fees", tmpValue );

        String[] constraints = serviceIdentification.getAccessConstraints();
        StringBuffer sb = new StringBuffer();
        if ( constraints == null || constraints.length > 0 ) {
            sb.append( "NONE" );
        } else {
            for ( int i = 0; i < constraints.length; ++i ) {
                String constraint = constraints[i];
                if ( constraint != null && "".equals( constraint.trim() ) ) {
                    sb.append( constraint );
                    if ( ( i + 1 ) < constraints.length ) {
                        sb.append( " " );
                    }
                }
            }
            if ( sb.length() == 0 ) {
                sb.append( "NONE" );
            }
        }
        XMLTools.appendElement( service, WFSNS, PRE_WFS + "AccessConstraints", sb.toString() );

    }

    /**
     * Appends the wfs:Capability element to the root element
     * 
     * @param root
     * @param operationsMetadata
     */
    private void appendCapability( Element root, WFSOperationsMetadata operationsMetadata, String defaultOnlineResource ) {
        Element capability = XMLTools.appendElement( root, WFSNS, PRE_WFS + "Capability" );
        Element request = XMLTools.appendElement( capability, WFSNS, PRE_WFS + "Request" );
        org.deegree.ogcwebservices.getcapabilities.Operation[] ops = operationsMetadata.getOperations();
        if ( ops != null && ops.length > 0 ) {
            for ( org.deegree.ogcwebservices.getcapabilities.Operation op : ops ) {
                String name = op.getName();
                if ( !( name == null || "".equals( name.trim() ) || "GetGMLObject".equals( name.trim() ) ) ) {
                    name = name.trim();

                    Element operation = XMLTools.appendElement( request, WFSNS, PRE_WFS + name );
                    if ( "DescribeFeatureType".equalsIgnoreCase( name ) ) {
                        Element sdl = XMLTools.appendElement( operation, WFSNS, PRE_WFS + "SchemaDescriptionLanguage" );
                        XMLTools.appendElement( sdl, WFSNS, PRE_WFS + "XMLSCHEMA" );
                    } else if ( "GetFeature".equalsIgnoreCase( name ) || "GetFeatureWithLock".equalsIgnoreCase( name ) ) {
                        Element resultFormat = XMLTools.appendElement( operation, WFSNS, PRE_WFS + "ResultFormat" );
                        XMLTools.appendElement( resultFormat, WFSNS, PRE_WFS + "GML2" );
                    }
                    DCPType[] dcpTypes = op.getDCPs();
                    if ( dcpTypes != null && dcpTypes.length > 0 ) {
                        for ( DCPType dcpType : dcpTypes ) {
                            appendDCPType( operation, dcpType, defaultOnlineResource );
                        }
                    } else {
                        appendDCPType( operation, null, defaultOnlineResource );
                    }
                }
            }
        }

    }

    /**
     * Appends the XML representation of the <code>wfs:FeatureTypeList</code>- section to the
     * passed <code>Element</code>.
     * 
     * @param root
     * @param featureTypeList
     */
    private void appendFeatureTypeList( Element root, FeatureTypeList featureTypeList ) {
        Element featureTypeListNode = XMLTools.appendElement( root, WFSNS, PRE_WFS + "FeatureTypeList" );
        Operation[] operations = featureTypeList.getGlobalOperations();
        if ( operations != null ) {
            Element operationsNode = XMLTools.appendElement( featureTypeListNode, WFSNS, PRE_WFS + "Operations" );
            for ( int i = 0; i < operations.length; i++ ) {
                XMLTools.appendElement( operationsNode, WFSNS, PRE_WFS + operations[i].getOperation() );
            }
        }
        WFSFeatureType[] featureTypes = featureTypeList.getFeatureTypes();
        if ( featureTypes != null ) {
            for ( int i = 0; i < featureTypes.length; i++ ) {
                appendWFSFeatureType( featureTypeListNode, featureTypes[i] );
            }
        }
    }

    /**
     * Appends the DCPType in the WFS namespace... pre-ows stuff.
     * 
     * @param operation
     *            to add the dcptype to.
     * @param type
     *            a bean containing necessary information if <code>null</code> a http-get/post dcp
     *            with the defaultonline resource will be inserted.
     * @param defaultOnlineResource
     *            if no dcpType is given or no URL were inserted in the config, this will be
     *            inserted.
     */
    private void appendDCPType( Element operation, DCPType type, String defaultOnlineResource ) {
        Element dcpType = XMLTools.appendElement( operation, WFSNS, PRE_WFS + "DCPType" );
        Element http = XMLTools.appendElement( dcpType, WFSNS, PRE_WFS + "HTTP" );
        boolean appendDefaultProtocol = true;
        if ( type != null ) {
            Protocol pr = type.getProtocol();
            if ( pr != null ) {
                if ( pr instanceof HTTP ) {
                    HTTP prot = (HTTP) pr;
                    URL[] getters = prot.getGetOnlineResources();
                    URL[] posters = prot.getPostOnlineResources();
                    if ( ( getters != null && getters.length > 0 ) ) {
                        for ( URL get : getters ) {
                            appendGetURL( http, get.toExternalForm() );
                        }
                    } else {
                        appendGetURL( http, defaultOnlineResource );
                    }
                    if ( posters != null && posters.length > 0 ) {
                        for ( URL post : posters ) {
                            appendPostURL( http, post.toExternalForm() );
                        }
                    } else {
                        appendPostURL( http, defaultOnlineResource );
                    }
                    appendDefaultProtocol = false;
                }
            }
        }
        if ( appendDefaultProtocol ) {
            appendGetURL( http, defaultOnlineResource );
            appendPostURL( http, defaultOnlineResource );
        }
    }

    private void appendGetURL( Element http, String resourceURL ) {
        Element get = XMLTools.appendElement( http, WFSNS, PRE_WFS + "Get" );
        get.setAttribute( "onlineResource", resourceURL );
    }

    private void appendPostURL( Element http, String resourceURL ) {
        Element post = XMLTools.appendElement( http, WFSNS, PRE_WFS + "Post" );
        post.setAttribute( "onlineResource", resourceURL );
    }

    private void appendKeyWords( Element root, Keywords[] keywords ) {
        if ( keywords != null && keywords.length > 0 ) {
            StringBuffer sb = new StringBuffer();
            for ( int k = 0; k < keywords.length; ++k ) {
                String[] words = keywords[k].getKeywords();
                if ( words != null && words.length > 0 ) {
                    for ( int i = 0; i < words.length; ++i ) {
                        sb.append( words[i] );
                        if ( ( i + 1 ) < words.length ) {
                            sb.append( " " );
                        }
                    }
                }
                if ( ( k + 1 ) < keywords.length ) {
                    sb.append( " " );
                }
            }
            XMLTools.appendElement( root, WFSNS, PRE_WFS + "Keywords", sb.toString() );
        }
    }

    /**
     * Appends the XML representation of the <code>WFSFeatureType</code> instance to the passed
     * <code>Element</code>.
     * 
     * @param root
     * @param featureType
     */
    private void appendWFSFeatureType( Element root, WFSFeatureType featureType ) {

        Element featureTypeNode = XMLTools.appendElement( root, WFSNS, PRE_WFS + "FeatureType" );

        if ( featureType.getName().getPrefix() != null ) {
            XMLTools.appendNSBinding( featureTypeNode, featureType.getName().getPrefix(),
                                      featureType.getName().getNamespace() );
        }
        XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "Name", featureType.getName().getPrefixedName() );
        XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "Title", featureType.getTitle() );
        String tmpValue = featureType.getAbstract();
        if ( tmpValue != null && !"".equals( tmpValue.trim() ) ) {
            XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "Abstract", tmpValue );
        }
        Keywords[] keywords = featureType.getKeywords();
        appendKeyWords( featureTypeNode, keywords );

        URI defaultSrs = featureType.getDefaultSRS();
        if ( defaultSrs != null ) {
            XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "SRS", defaultSrs.toASCIIString() );
        } else if ( featureType.getOtherSrs() != null && featureType.getOtherSrs().length > 0 ) {
            for ( URI srs : featureType.getOtherSrs() ) {
                if ( srs != null ) {
                    XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "SRS", srs.toASCIIString() );
                    break;
                }
            }
        } else {
            // defaulting to EPSG:4326 is this correct????
            LOG.logDebug( "Found no default- or other-SRS, setting to EPGS:4326, is this correct?" );
            XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "SRS", "EPSG:4326" );
        }
        Operation[] operations = featureType.getOperations();
        if ( operations != null && operations.length > 0 ) {
            for ( Operation op : operations ) {
                if ( op != null && !"".equals( op.getOperation().trim() )
                     && !"GetGMLObject".equalsIgnoreCase( op.getOperation().trim() ) ) {
                    Element opEl = XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "Operations" );
                    XMLTools.appendElement( opEl, WFSNS, PRE_WFS + op.getOperation().trim() );

                }
            }
        }

        Envelope[] wgs84BoundingBoxes = featureType.getWgs84BoundingBoxes();
        for ( Envelope bbox : wgs84BoundingBoxes ) {
            if ( bbox != null && bbox.getMin().getCoordinateDimension() == 2 ) {
                Element latlon = XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "LatLongBoundingBox" );
                Position min = bbox.getMin();
                Position max = bbox.getMax();
                latlon.setAttribute( "minx", Double.toString( min.getX() ) );
                latlon.setAttribute( "miny", Double.toString( min.getY() ) );
                latlon.setAttribute( "maxx", Double.toString( max.getX() ) );
                latlon.setAttribute( "maxy", Double.toString( max.getY() ) );
            }
        }
        MetadataURL[] mdURLs = featureType.getMetadataUrls();
        if ( mdURLs != null ) {
            for ( MetadataURL mdURL : mdURLs ) {
                if ( mdURL != null && mdURL.getOnlineResource() != null ) {
                    // first check if the format and type are acceptable.
                    String format = mdURL.getFormat();
                    boolean formatOK = false;
                    if ( format != null && !"".equals( format.trim() ) ) {
                        format = format.trim();
                        if ( "XML".equals( format ) || "SGML".equals( format ) || "TXT".equals( format ) ) {
                            formatOK = true;
                        }
                    }

                    String type = mdURL.getType();
                    boolean typeOK = false;
                    if ( formatOK && type != null && !"".equals( type.trim() ) ) {
                        type = type.trim();
                        if ( "TC211".equals( type ) || "FGDC".equals( type ) ) {
                            typeOK = true;
                        }
                    }
                    if ( formatOK && typeOK ) {
                        Element metadata = XMLTools.appendElement( featureTypeNode, WFSNS, PRE_WFS + "MetadataURL",
                                                                   mdURL.getOnlineResource().toExternalForm() );
                        metadata.setAttribute( "type", type );
                        metadata.setAttribute( "format", format );
                    }
                }
            }
        }
    }

    private String checkForEmptyValue( String value, String elementName, String defaultValue ) {
        if ( value == null || "".equals( value.trim() ) ) {
            LOG.logError( Messages.getMessage( "WFS_MISSING_REQUIRED_ELEMENT", elementName ) );
            value = defaultValue;
        }
        return value;
    }
}