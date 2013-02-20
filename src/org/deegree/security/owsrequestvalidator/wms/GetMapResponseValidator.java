//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsrequestvalidator/wms/GetMapResponseValidator.java $
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
package org.deegree.security.owsrequestvalidator.wms;

import org.deegree.framework.util.MimeTypeMapper;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.security.drm.model.User;
import org.deegree.security.owsproxy.Request;
import org.deegree.security.owsrequestvalidator.Policy;
import org.deegree.security.owsrequestvalidator.ResponseValidator;

/**
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10559 $, $Date: 2008-03-12 09:35:07 +0100 (Wed, 12 Mar 2008) $
 */
class GetMapResponseValidator extends ResponseValidator {

    /**
     * @param policy
     */
    public GetMapResponseValidator( Policy policy ) {
        super( policy );
    }

    /**
     * validates the passed object as a response to a OWS request. The validity of the response may
     * is assigned to specific user rights. If the passed user is <>null this will be evaluated.
     * <br>
     * the reponse may contain three valid kinds of objects:
     * <ul>
     * <li>a serialized image
     * <li>a xml encoded exception
     * <li>a svg-encoded vector image
     * </ul>
     * Each of these types can be identified by the mime-type of the response that is also passed to
     * the method. <br>
     * If something basic went wrong it is possible that not further specified kind of object is
     * passed as response. In this case the method will throw an
     * <tt>InvalidParameterValueException</tt> to avoid sending bad responses to the client.
     * 
     * @param service
     *            service which produced the response (WMS, WFS ...)
     * @param response
     * @param mime
     *            mime-type of the response
     * @param user
     * @return a byte array containing the response or the given response if isAny is given.
     * @throws InvalidParameterValueException
     * @see GetMapRequestValidator#validateRequest(OGCWebServiceRequest, User)
     */
    @Override
    public byte[] validateResponse( String service, byte[] response, String mime, User user )
                            throws InvalidParameterValueException {

        Request req = policy.getRequest( service, "GetMap" );
        // request is valid because no restrictions are made
        if ( req.isAny() )
            return response;

        //Condition postCondition = req.getPostConditions();

        if ( MimeTypeMapper.isKnownImageType( mime ) ) {
            response = validateImage( response, mime, user );
        } else if ( MimeTypeMapper.isKnownOGCType( mime ) ) {
            // if the mime-type isn't an image type but a known
            // OGC mime-type it must be an XML document.
            // probably it is an exception but it also could be
            // a GML document
            response = validateXML( response, mime, user );
        } else if ( mime.equals( "text/xml" ) ) {
            // if the mime-type isn't an image type but 'text/xml'
            // it could be an exception
            response = validateXML( response, mime, user );
        } else {
            throw new InvalidParameterValueException( UNKNOWNMIMETYPE + mime );
        }

        return response;
    }

    /**
     * Does nothing should validate the passed object/image to be valid against the policy
     * 
     * @param image
     * @param mime
     * @param user
     * @return the image parameter.
     */
    private byte[] validateImage( byte[] image, String mime, User user ) {
        // TODO
        // define useful post-validation for images
        // at the moment everything is valid

        return image;
    }

    /**
     * Does nothing but should validate the passed object/xml to be valid against the policy
     * 
     * @param xml
     * @param mime
     * @param user
     * @return the xml parameter.
     */
    private byte[] validateXML( byte[] xml, String mime, User user ) {
        // TODO
        // define useful post-validation for xml-documents
        // at the moment everything is valid
        return xml;
    }
}