//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/sos/control/AbstractSOSListener.java $
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
package org.deegree.portal.standard.sos.control;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.deegree.enterprise.WebUtils;
import org.deegree.enterprise.control.AbstractListener;
import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.xml.XMLTools;
import org.deegree.portal.standard.sos.SOSClientException;
import org.deegree.portal.standard.sos.configuration.SOSClientConfiguration;
import org.w3c.dom.Document;

/**
 * ...
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * 
 */
public abstract class AbstractSOSListener extends AbstractListener {

    /**
     * Validates a request
     * @param mc the remote procedure call bean
     * @throws SOSClientException
     */
    protected abstract void validateRequest( RPCMethodCall mc )
                            throws SOSClientException;

/**
 * Create a request
 * @param mc the remote procedure call bean
 * @return a String containing the request.
 * @throws SOSClientException
 */
    protected abstract String createRequest( RPCMethodCall mc )
                            throws SOSClientException;

    /**
     * Create a Data from the call and the given map.
     * @param mc the remote procedure call bean
     * @param map containing the document nodes.
     * @return the data bean.
     * @throws SOSClientException
     */
    protected abstract Object createData( RPCMethodCall mc, HashMap<String,Document> map )
                            throws SOSClientException;

    /**
     * sets the parameter as ServletRequest attribute to enable access to the result for the next
     * page
     * 
     * @param data
     *            param/result
     */
    protected abstract void setNextPageData( Object data );

    @Override
    public void actionPerformed( FormEvent e ) {

        RPCWebEvent rpcEvent = (RPCWebEvent) e;
        RPCMethodCall mc = rpcEvent.getRPCMethodCall();

        try {
            validateRequest( mc );
        } catch ( Exception ex ) {
            gotoErrorPage( "Invalid Sensor Observation Service DescribePlatform request: " + ex.toString() );
            return;
        }

        String request = null;
        try {
            request = createRequest( mc );
        } catch ( Exception ex ) {
            gotoErrorPage( "Couldn't create Sensor Observation Service DescribePlatform request: " + ex.toString() );
            return;
        }

        HashMap<String, Document> map = null;
        try {
            map = performRequest( request );
        } catch ( Exception ex ) {
            gotoErrorPage( "Couldn't perform Sensor Observation Service DescribePlatform request: " + ex.toString() );
            return;
        }

        Object data = null;
        try {
            data = createData( mc, map );
        } catch ( Exception ex ) {
            gotoErrorPage( "Couldn't format Sensor Observation Service result: " + ex.toString() );
            return;
        }

        // setParamForNextPage( key, data );
        setNextPageData( data );
    }

    // FIXME make return type a "Map"
    protected HashMap<String,Document> performRequest( String request )
                            throws SOSClientException {

        // FIXME or I'm not sure whether we should return a hash map here. Doc will do too
        HashMap<String, Document> map = new HashMap<String, Document>();

        SOSClientConfiguration conf = SOSClientConfiguration.getInstance();
        String[] sosResources = conf.getSOSNames();
        // perform a request for each registered sensor observation service
        for ( int i = 0; i < sosResources.length; i++ ) {
            URL url = conf.getSOSAddress( sosResources[i] );

            try {

                HttpClient httpclient = new HttpClient();
                httpclient = WebUtils.enableProxyUsage( httpclient, url );
                httpclient.getHttpConnectionManager().getParams().setSoTimeout( 30000 );

                PostMethod httppost = new PostMethod( url.toString() );
                StringRequestEntity sre = new StringRequestEntity( request );
                httppost.setRequestEntity( sre );
                httpclient.executeMethod( httppost );

                java.io.InputStream is = httppost.getResponseBodyAsStream();
                InputStreamReader inputStreamRdr = new InputStreamReader( is );
                Document doc = XMLTools.parse( inputStreamRdr );

                map.put( sosResources[i], doc );

            } catch ( Exception e ) {
                throw new SOSClientException( "Couldn't connect to Observation Service at " + url.toString(), e );
            }
        }

        return map;
    }

}