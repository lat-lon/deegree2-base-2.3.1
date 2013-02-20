/*----------------    FILE HEADER  ------------------------------------------
 This file is part of adv ebrim project.
 Copyright (C) 2007 by:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 ---------------------------------------------------------------------------*/
package de.latlon.adv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLTools;
import org.deegree.framework.xml.XSLTDocument;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * The <code>EBRIMTransformer</code> class
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: bezema $
 * 
 * @version $Revision: 1.6 $, $Date: 2007-06-11 16:40:49 $
 * 
 */

public class EBRIMTransformer {
    private static ILogger LOG = LoggerFactory.getLogger( EBRIMTransformer.class );

    /**
     * simple main method to test the xslt transforming of ebrim to wfs
     * 
     * @param args
     *            the commandline arguments
     */
    public static void main( String[] args ) {
        try {
            
//            String xsltScript= "scripts/xslt/outEBRIM.xsl";
//            String inputXML= "resources/output/GetFeatureResponse_all_registry_objects.xml";
            
            //String inputXML= "resources/output/GetFeatureResponse_classification_node.xml";
            String xsltScript= "scripts/xslt/ebrim/inEBRIM.xsl";
            String inputXML= "resources/requests/csw/Transaction/xml/very_small_registry_package.xml";
            XMLFragment transactionInsertDocument = new XMLFragment( new File( inputXML ) );
            LOG.logInfo( "Input GetRecords request:\n" + transactionInsertDocument.getAsPrettyString() );
            Element n = transactionInsertDocument.getRootElement();
            try {
                LOG.logInfo( "the rim prefix is bound to: " + XMLTools.getNamespaceForPrefix( "rim", n ) );
            } catch ( URISyntaxException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // incoming GetRecord request must be transformed to a GetFeature
            // request because the underlying 'data engine' of the CSW is a WFS
            XSLTDocument xslSheet = new XSLTDocument( new File( xsltScript ).toURI().toURL() );
            Map<String, String> params = new HashMap<String, String>();
            params.put( "REQUEST_NAME", "GetRecords" );
            params.put( "VERSION", "2.0.0" );
            params.put( "SEARCH_STATUS", "bla" );
            params.put( "ELEMENT_SET", "full" );
            params.put( "RECORD_SCHEMA", "bla" );
            params.put( "RECORDS_MATCHED", "10" );
            params.put( "RECORDS_RETURNED", "10" );
            params.put( "NEXT_RECORD", "0" );

            LOG.logInfo( "Transforming: " + inputXML +" with script: " + xsltScript );
            XMLFragment getFeatureDocument = xslSheet.transform( transactionInsertDocument,
                                                                 "http://deegree.org",
                                                                 null,
                                                                 params );
            LOG.logInfo( "Writing generated output to /dev/shm/out.xml");
            getFeatureDocument.prettyPrint( new FileWriter( new File( "/dev/shm/out.xml" ) ) );
            LOG.logInfo( "Generated WFS GetFeature request:\n" + getFeatureDocument.getAsPrettyString() );
        } catch ( TransformerException e ) {
            e.printStackTrace();
            String msg = "Can't transform GetRecord request to WFS GetFeature request: " + e.getMessage();
            LOG.logError( msg, e );
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
            String msg = "Can't transform GetRecord request to WFS GetFeature request: " + e.getMessage();
            LOG.logError( msg, e );
        } catch ( IOException e ) {
            e.printStackTrace();
            String msg = "Can't transform GetRecord request to WFS GetFeature request: " + e.getMessage();
            LOG.logError( msg, e );
        } catch ( SAXException e ) {
            e.printStackTrace();
            String msg = "Can't transform GetRecord request to WFS GetFeature request: " + e.getMessage();
            LOG.logError( msg, e );
        }
    }
}
