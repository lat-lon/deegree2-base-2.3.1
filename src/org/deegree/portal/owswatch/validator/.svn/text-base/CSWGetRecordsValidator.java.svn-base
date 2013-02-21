//$$Header: $$
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

package org.deegree.portal.owswatch.validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;

import org.apache.commons.httpclient.HttpMethodBase;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.w3c.dom.Document;

import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;

/**
 * A specific implementation of AbstractValidator
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:51 $
 */
public class CSWGetRecordsValidator extends AbstractValidator implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 653569280799419300L;

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.portal.owswatch.validator.AbstractValidator#validateAnswer(org.apache.commons.httpclient.HttpMethodBase,
     *      int)
     */
    @Override
    public ValidatorResponse validateAnswer( HttpMethodBase method, int statusCode ) {

        String contentType = method.getResponseHeader( "Content-Type" ).getValue();
        String lastMessage = null;
        Status status = null;

        if ( !contentType.contains( "xml" ) ) {
            status = Status.RESULT_STATE_UNEXPECTED_CONTENT;
            lastMessage = StringTools.concat( 100, "Error: Response Content is ", contentType, " not xml" );
            return new ValidatorResponse( lastMessage, status );
        }

        String xml = null;
        try {
            InputStream stream = method.getResponseBodyAsStream();
            stream.reset();
            xml = parseStream( stream );
        } catch ( IOException e ) {
            status = Status.RESULT_STATE_BAD_RESPONSE;
            lastMessage = status.getStatusMessage();
            return new ValidatorResponse( lastMessage, status );
        }

        if ( xml.length() == 0 ) {
            status = Status.RESULT_STATE_BAD_RESPONSE;
            lastMessage = "Error: XML Response is empty";
            return new ValidatorResponse( lastMessage, status );
        }

        if ( xml.contains( "ExceptionReport" ) ) {
            validateXmlServiceException( method );
            return new ValidatorResponse( lastMessage, status );
        }
        // If its an xml, and there's no service exception, then don't really parse the xml,
        // we assume that its well formed, since there might be huge xmls, which would take time to be parsed
        status = Status.RESULT_STATE_AVAILABLE;
        lastMessage = status.getStatusMessage();
        return new ValidatorResponse( lastMessage, status );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.portal.owswatch.validator.AbstractValidator#validateXmlServiceException(org.apache.commons.httpclient.HttpMethodBase)
     */
    @Override
    protected ValidatorResponse validateXmlServiceException( HttpMethodBase method ) {

        Document doc = null;
        String lastMessage = null;
        Status status = null;

        try {
            InputStream stream = method.getResponseBodyAsStream();
            stream.reset();
            doc = instantiateParser().parse( stream );
        } catch ( Exception e ) {
            status = Status.RESULT_STATE_INVALID_XML;
            lastMessage = "Error: MalFormed XML Response";
            return new ValidatorResponse( lastMessage, status );
        }
        try {
            NamespaceContext cnxt = CommonNamespaces.getNamespaceContext();
            URI owsns = CommonNamespaces.OWSNS;
            String prefix = doc.lookupPrefix( owsns.toASCIIString() );
            StringBuilder builder = new StringBuilder( 100 );
            builder.append( "./" );
            if ( prefix != null && prefix.length() > 0 ) {
                builder.append( prefix ).append( ":" );
                cnxt.addNamespace( prefix, owsns );
            }

            builder.append( "Exception" );
            status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
            lastMessage = XMLTools.getNodeAsString( doc.getDocumentElement(), builder.toString(), cnxt,
                                                    "Service Unavailable. Unknown error" );
            return new ValidatorResponse( lastMessage, status );
        } catch ( XMLParsingException e ) {
            lastMessage = "Service Unavailable";
            status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
            return new ValidatorResponse( lastMessage, status );
        }
    }
}
