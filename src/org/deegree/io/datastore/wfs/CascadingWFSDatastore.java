//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/wfs/CascadingWFSDatastore.java $
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
package org.deegree.io.datastore.wfs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

import javax.xml.transform.TransformerException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.deegree.datatypes.QualifiedName;
import org.deegree.enterprise.WebUtils;
import org.deegree.framework.concurrent.ExecutionFinishedEvent;
import org.deegree.framework.concurrent.Executor;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XSLTDocument;
import org.deegree.i18n.Messages;
import org.deegree.io.datastore.Datastore;
import org.deegree.io.datastore.DatastoreException;
import org.deegree.io.datastore.DatastoreTransaction;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureFactory;
import org.deegree.model.feature.GMLFeatureCollectionDocument;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OWSUtils;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.wfs.XMLFactory;
import org.deegree.ogcwebservices.wfs.capabilities.WFSCapabilities;
import org.deegree.ogcwebservices.wfs.capabilities.WFSCapabilitiesDocument;
import org.deegree.ogcwebservices.wfs.operation.GetFeature;
import org.deegree.ogcwebservices.wfs.operation.Query;
import org.deegree.ogcwebservices.wfs.operation.GetFeature.RESULT_TYPE;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 10506 $, $Date: 2008-03-06 17:50:33 +0100 (Thu, 06 Mar 2008) $
 */
public class CascadingWFSDatastore extends Datastore {

    ILogger LOG = LoggerFactory.getLogger( CascadingWFSDatastore.class );

    private static Map<URL, WFSCapabilities> wfsCapabilities;
    static {
        if ( wfsCapabilities == null ) {
            wfsCapabilities = new HashMap<URL, WFSCapabilities>();
        }
    }

    @Override
    public CascadingWFSAnnotationDocument getAnnotationParser() {
        return new CascadingWFSAnnotationDocument();
    }

    @Override
    public void close()
                            throws DatastoreException {
        // is already closed
    }

    @Override
    public FeatureCollection performQuery( Query query, MappedFeatureType[] rootFts, DatastoreTransaction context )
                            throws DatastoreException, UnknownCRSException {
        return performQuery( query, rootFts );
    }

    @Override
    public FeatureCollection performQuery( Query query, MappedFeatureType[] rootFts )
                            throws DatastoreException, UnknownCRSException {

        GetFeature getFeature = GetFeature.create( "1.1.0", "ID", RESULT_TYPE.RESULTS, "text/xml; subtype=gml/3.1.1",
                                                   "", query.getMaxFeatures(), query.getStartPosition(), -1, -1,
                                                   new Query[] { query } );
        XMLFragment gfXML = null;
        try {
            gfXML = XMLFactory.export( getFeature );
        } catch ( IOException e ) {
            LOG.logError( e.getMessage(), e );
            throw new DatastoreException( e.getMessage() );
        } catch ( XMLParsingException e ) {
            LOG.logError( e.getMessage(), e );
            throw new DatastoreException( e.getMessage() );
        }

        // get URL that is target of a GetFeature request
        CascadingWFSDatastoreConfiguration config = (CascadingWFSDatastoreConfiguration) this.getConfiguration();
        WFSDescription[] wfs = config.getWFSDescription();
        List<Callable<FeatureCollection>> queryTasks = new ArrayList<Callable<FeatureCollection>>( wfs.length );
        int timeout = 0;
        for ( int i = 0; i < wfs.length; i++ ) {
            QueryTask task = new QueryTask( gfXML, wfs[i] );
            queryTasks.add( task );
            timeout += wfs[i].getTimeout();
        }

        List<ExecutionFinishedEvent<FeatureCollection>> finishedEvents = null;
        try {
            finishedEvents = Executor.getInstance().performSynchronously( queryTasks, timeout );
        } catch ( InterruptedException e ) {
            LOG.logError( e.getMessage(), e );
            throw new DatastoreException( Messages.getMessage( "WFS_CASCDS_PERFORM_GF" ), e );
        }

        return mergeResults( getFeature.getId(), finishedEvents );
    }

    /**
     * Merges the results of the request subparts into one feature collection.
     * 
     * @param fcid
     *            id of the new (result) feature collection
     * @param finishedEvents
     * @return feature collection containing all features from all responses
     * @throws OGCWebServiceException
     */
    private FeatureCollection mergeResults( String fcid, List<ExecutionFinishedEvent<FeatureCollection>> finishedEvents )
                            throws DatastoreException {

        FeatureCollection result = null;

        try {
            for ( ExecutionFinishedEvent<FeatureCollection> event : finishedEvents ) {
                if ( result == null ) {
                    result = event.getResult();
                } else {
                    result.addAll( event.getResult() );
                }
            }
        } catch ( CancellationException e ) {
            LOG.logError( e.getMessage(), e );
            String msg = Messages.getMessage( "WFS_GET_FEATURE_TIMEOUT", e.getMessage() );
            throw new DatastoreException( msg, e );
        } catch ( Throwable t ) {
            LOG.logError( t.getMessage(), t );
            String msg = Messages.getMessage( "WFS_GET_FEATURE_BACKEND", t.getMessage() );
            throw new DatastoreException( msg, t );
        }

        result.setId( fcid );
        result.setAttribute( "numberOfFeatures", "" + result.size() );
        return result;
    }

    WFSCapabilities getWFSCapabilities( URL url )
                            throws DatastoreException {
        String href = OWSUtils.validateHTTPGetBaseURL( url.toExternalForm() );
        href = href + "request=GetCapabilities&version=1.1.0&service=WFS";

        LOG.logDebug( "requested capabilities: ", href );

        try {
            url = new URL( href );
        } catch ( MalformedURLException e1 ) {
            e1.printStackTrace();
        }

        WFSCapabilities caps = wfsCapabilities.get( url );
        if ( caps == null ) {
            // access capabilities if not already has been loaded
            WFSCapabilitiesDocument cd = new WFSCapabilitiesDocument();
            try {
                cd.load( url );
            } catch ( IOException e ) {
                LOG.logError( e.getMessage(), e );
                throw new DatastoreException( e.getMessage() );
            } catch ( SAXException e ) {
                LOG.logError( e.getMessage(), e );
                throw new DatastoreException( e.getMessage() );
            }
            try {
                caps = (WFSCapabilities) cd.parseCapabilities();
            } catch ( InvalidCapabilitiesException e ) {
                LOG.logError( e.getMessage(), e );
                throw new DatastoreException( e.getMessage() );
            }
            wfsCapabilities.put( url, caps );
        }
        return caps;
    }

    // ///////////////////////////////////////////////////////////////////////////
    // inner classes
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * Inner class for performing queries on a datastore.
     */
    private class QueryTask implements Callable<FeatureCollection> {

        private XMLFragment getFeature;

        private WFSDescription wfs;

        /**
         * 
         * @param getFeature
         * @param wfs
         */
        QueryTask( XMLFragment getFeature, WFSDescription wfs ) {
            this.getFeature = getFeature;
            this.wfs = wfs;
        }

        /**
         * Performs the associated {@link Query} and returns the result.
         * 
         * @return resulting feature collection
         * @throws Exception
         */
        public FeatureCollection call()
                                throws Exception {

            URL url = OWSUtils.getHTTPPostOperationURL( getWFSCapabilities( wfs.getUrl() ), GetFeature.class );

            // filter request if necessary
            XSLTDocument inFilter = wfs.getInFilter();
            if ( inFilter != null ) {
                try {
                    getFeature = inFilter.transform( getFeature );
                } catch ( TransformerException e ) {
                    LOG.logError( e.getMessage(), e );
                    throw new DatastoreException( e.getMessage() );
                }
            }

            if ( isFeatureTypeSupported( getFeature, wfs.getUrl() ) ) {

                InputStream is = null;
                FeatureCollection fc = null;
                try {
                    // perform GetFeature request against cascaded WFS
                    HttpClient client = new HttpClient();
                    client = WebUtils.enableProxyUsage( client, url );
                    client.getHttpConnectionManager().getParams().setSoTimeout( wfs.getTimeout() );
                    PostMethod post = new PostMethod( url.toExternalForm() );
                    StringRequestEntity se = new StringRequestEntity( getFeature.getAsString(), "text/xml",
                                                                      CharsetUtils.getSystemCharset() );
                    post.setRequestEntity( se );
                    client.executeMethod( post );
                    is = post.getResponseBodyAsStream();
                } catch ( Exception e ) {
                    throw new DatastoreException( Messages.getMessage( "DATASTORE_WFS_ACCESS", url ) );
                }

                // read result as GMLFeatureColllection
                GMLFeatureCollectionDocument fcd = new GMLFeatureCollectionDocument( true );
                try {
                    fcd.load( is, url.toExternalForm() );
                } catch ( Exception e ) {
                    if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream( 50000 );
                        int c = 0;
                        while ( c > -1 ) {
                            c = is.read();
                            bos.write( c );
                        }
                        byte[] b = bos.toByteArray();
                        bos.close();
                        System.out.println( new String( b ) );
                    }
                    LOG.logError( e.getMessage(), e );
                    throw new DatastoreException( e.getMessage() );
                } finally {
                    try {
                        is.close();
                    } catch ( IOException shouldNeverHappen ) {
                        // and is ignored
                    }
                }

                // filter result if necessary
                XSLTDocument outFilter = wfs.getOutFilter();
                if ( outFilter != null ) {
                    try {
                        XMLFragment xml = outFilter.transform( fcd );
                        fcd = new GMLFeatureCollectionDocument();
                        fcd.setRootElement( xml.getRootElement() );
                    } catch ( TransformerException e ) {
                        LOG.logError( e.getMessage(), e );
                        throw new DatastoreException( e.getMessage() );
                    }
                }
                try {
                    fc = fcd.parse();
                } catch ( XMLParsingException e ) {
                    LOG.logError( e.getMessage(), e );
                    throw new DatastoreException( e.getMessage() );
                }

                return fc;
            }
            return FeatureFactory.createFeatureCollection( "ID", 1 );
        }

        /**
         * @return true if the WFS reachable through the passed URL supports all feature types
         * targeted by the passed GetFeature request.
         * 
         * @param getFeature
         * @param url
         * @throws OGCWebServiceException
         * @throws DatastoreException
         */
        private boolean isFeatureTypeSupported( XMLFragment getFeature, URL url )
                                throws OGCWebServiceException, DatastoreException {

            WFSCapabilities caps = getWFSCapabilities( url );

            GetFeature gf = GetFeature.create( "ID" + System.currentTimeMillis(), getFeature.getRootElement() );
            Query[] queries = gf.getQuery();
            for ( int i = 0; i < queries.length; i++ ) {
                QualifiedName featureType = queries[i].getTypeNames()[0];
                if ( caps.getFeatureTypeList().getFeatureType( featureType ) == null ) {
                    return false;
                }
            }
            return true;
        }
    }
}