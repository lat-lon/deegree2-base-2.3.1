//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/csw/MetadataTransformer.java $
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

package org.deegree.portal.standard.csw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.portal.standard.context.control.ContextLoadListener;

/**
 * A <code>${type_name}</code> class.<br/> TODO class description
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: melmasry $
 * 
 * @version 2.0, $Revision: 13988 $, $Date: 2008-09-18 14:40:03 +0200 (Do, 18. Sep 2008) $
 * 
 * @since 2.0
 */
public class MetadataTransformer {

    private static final ILogger LOG = LoggerFactory.getLogger( ContextLoadListener.class );

    /**
     * The <code>Transformer</code> object used in the transformation of a map context xml to html.
     */
    private Transformer transformer = null;

    /**
     * Creates a new MetadataTransformer and initializes it with the given <code>file</code> (path and name).
     * 
     * @param filePathName
     * @throws FileNotFoundException,
     *             if filePathName does not point to an existing file.
     */
    public MetadataTransformer( String filePathName ) throws FileNotFoundException {
        initTransformer( filePathName );
    }

    /**
     * @param filePathName
     * @throws FileNotFoundException
     */
    private void initTransformer( String filePathName )
                            throws FileNotFoundException {

        InputStream xslInputStream = new FileInputStream( filePathName );

        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            transformer = tFactory.newTransformer( new StreamSource( xslInputStream ) );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage() );
        }
    }

    /**
     * Transforms the context pointed to by <code>context</code> into html using <code>xsltURL</code>
     * 
     * @param metadataXml
     *            The <code>Reader</code> containing the xml document to be transformed.
     * @param catalog
     *            The name of the catalog.
     * @param serviceCatalogs
     * @param hits
     *            The number of records matched for this catalog.
     * @param startPosition
     *            The position to start displaying the matched records from.
     * @param metaVersion
     *            The version of metadata to transform ( list, overview, detailed ).
     * @return Returns result of transformation.
     * @throws TransformerException
     * @throws IOException
     */
    public String transformMetadata( Reader metadataXml, String catalog, String[] serviceCatalogs, int hits,
                                     int startPosition, String metaVersion )
                            throws TransformerException, IOException {

        if ( transformer == null ) {
            throw new IOException( Messages.getMessage( "IGEO_STD_CSW_TRANSFORMER_ERROR" ) );
        }

        StringWriter sw = new StringWriter();
        StreamResult strmResult = new StreamResult( sw );
        StreamSource xmlSrc = new StreamSource( metadataXml );

        // turn array of Strings into one comma-separated String
        StringBuffer sb = new StringBuffer();
        if ( serviceCatalogs != null ) {
            for ( int i = 0; i < serviceCatalogs.length; i++ ) {
                sb.append( serviceCatalogs[i] );
                if ( i < serviceCatalogs.length - 1 ) {
                    sb.append( "," );
                }
            }
        }

        // setting global variables for xslt-scripts
        transformer.setParameter( "CATALOG", catalog );
        transformer.setParameter( "SERVICECATALOGS", sb.toString() );
        transformer.setParameter( "HITS", new Integer( hits ) );
        transformer.setParameter( "STARTPOS", new Integer( startPosition ) );
        transformer.setParameter( "METAVERSION", metaVersion );

        transformer.transform( xmlSrc, strmResult );
        try {
            sw.close();
        } catch ( IOException e ) {
            LOG.logError( "Unable to close string writer.\n" );
        }

        return sw.toString();
    }

    @Override
    public String toString() {
        return transformer.toString();
    }

}
