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

package org.deegree.portal.owswatch;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.mail.EMailMessage;
import org.deegree.framework.mail.MailHelper;
import org.deegree.framework.mail.MailMessage;
import org.deegree.framework.mail.UnknownMimeTypeException;

/**
 * This class responsible for sending emails to the registered users in case of faulty tests to the services
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: tonnhofer $
 * 
 * @version $Revision: 1.2 $, $Date: 2008-03-18 09:24:05 $
 */
public class EmailSender implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6179048752410881409L;

    private static final ILogger LOG = LoggerFactory.getLogger( EmailSender.class );

    private String mailFrom = null;

    private String mailServer = null;

    private List<String> receivers = null;

    /**
     * @param mailFrom
     * @param mailServer
     * @param receivers
     */
    public EmailSender( String mailFrom, String mailServer, List<String> receivers ) {

        this.mailFrom = mailFrom;
        this.mailServer = mailServer;
        this.receivers = receivers;
    }

    protected String createEmailBody( ServiceConfiguration serviceConfiguration, ValidatorResponse response,
                                      String protocolHttpaddress ) {

        // create body (allways the same):
        StringBuffer body = new StringBuffer( 100 );
        String protHref = Messages.getMessage( "SendMail.protocolAddress", protocolHttpaddress, protocolHttpaddress );
        body.append( Messages.getString( "SendMail.header" ) );
        body.append( Messages.getString( "SendMail.addressee" ) );
        body.append( Messages.getMessage( "SendMail.messageBody", serviceConfiguration.getServiceName() ) );
        body.append( Messages.getMessage( "SendMail.errorLog", serviceConfiguration.getOnlineResource(),
                                          serviceConfiguration.getServiceVersion(),
                                          serviceConfiguration.getRequestType(), response.getLastTest().toString(),
                                          response.getMessage(), protHref ) );
        body.append( Messages.getMessage( "SendMail.closingWords" ) );
        body.append( Messages.getString( "SendMail.signer" ) );
        body.append( Messages.getString( "SendMail.footer" ) );
        return body.toString();
    }

    /**
     * this method creates a mail from text fragments in the messages_en.properties and sends it to the owsWatch admin
     * as defined in the WEB-INF/conf/owswatch/config.xml file.
     * 
     * If sending the mail fails, an error messages is returned, else a success message is returned.
     * 
     * @param serviceConfiguration
     * @param response
     * @param protocolHttpaddress
     * 
     */
    public void createAndSendMail( ServiceConfiguration serviceConfiguration, ValidatorResponse response,
                                   String protocolHttpaddress ) {

        String body = createEmailBody( serviceConfiguration, response, protocolHttpaddress );
        String error = "";
        String subject = Messages.getString( "SendMail.subject" );

        Iterator<String> it = receivers.iterator();
        while ( it.hasNext() ) {
            // send message to the user
            EMailMessage mm = new EMailMessage( mailFrom, it.next(), subject, body.toString() );
            try {
                mm.setMimeType( MailMessage.TEXT_HTML );
            } catch ( UnknownMimeTypeException e ) {
                String errorMsg = "ERROR_UNDEFINED_TYPE";
                LOG.logError( errorMsg );
                continue;
            }

            try {
                MailHelper.createAndSendMail( mm, mailServer );
            } catch ( Exception e ) {
                error = Messages.arrayToString(
                                                new String[] {
                                                              e.getLocalizedMessage(),
                                                              org.deegree.portal.owswatch.Messages.getMessage(
                                                                                                               "ERROR_SEND_MAIL",
                                                                                                               receivers,
                                                                                                               mailFrom,
                                                                                                               mailServer ),
                                                              response.getMessage() }, "\n" );
                LOG.logError( error );
            }
        }
    }
}
