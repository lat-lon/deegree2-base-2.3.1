//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/context/control/ResetContextListener.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 University of Bonn
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

 Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: klaus.greve@uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.portal.standard.context.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.deegree.enterprise.control.FormEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.i18n.Messages;
import org.deegree.portal.Constants;
import org.deegree.portal.context.ViewContext;
import org.deegree.portal.context.XMLFactory;

/**
 * TODO add documentation here
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9348 $, $Date: 2007-12-27 17:59:14 +0100 (Thu, 27 Dec 2007) $
 */
public class ResetContextListener extends AbstractContextListener {

    private static final ILogger LOG = LoggerFactory.getLogger( ResetContextListener.class );

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.enterprise.control.WebListener#actionPerformed(org.deegree.enterprise.control.FormEvent)
     */
    @Override
    public void actionPerformed( FormEvent event ) {

        

        String xslFilename = "file://" + getHomePath() + ContextSwitchListener.DEFAULT_CTXT2HTML;
        String newHtml;
        try {
            newHtml = doTransformContext( xslFilename );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            gotoErrorPage( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_RESET_CNTXT", e.getMessage() ) );
            
            return;
        }
        // get the servlet path using the session
        HttpSession session = ( (HttpServletRequest) this.getRequest() ).getSession();
        session.setAttribute( ContextSwitchListener.NEW_CONTEXT_HTML, newHtml );

        
    }

    /**
     * Transforms the context into html using <code>xsl</code>.
     * 
     * @param xsl
     *            the transformation xml
     * @return the transformed context
     * @throws Exception
     */
    protected String doTransformContext( String xsl )
                            throws Exception {

        HttpSession session = ( (HttpServletRequest) getRequest() ).getSession( true );
        ViewContext vc = (ViewContext) session.getAttribute( Constants.CURRENTMAPCONTEXT );
        XMLFragment xml = XMLFactory.export( vc );

        return transformToHtmlMapContext( xml, xsl );
    }
}