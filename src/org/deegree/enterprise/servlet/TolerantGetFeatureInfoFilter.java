//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/TolerantGetFeatureInfoFilter.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2007 by:
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

package org.deegree.enterprise.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * <code>TolerantGetFeatureInfoFilter</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 13105 $, $Date: 2008-07-21 12:09:54 +0200 (Mo, 21. Jul 2008) $
 */
public class TolerantGetFeatureInfoFilter implements Filter {

    public void destroy() {
        // nothing to destroy
    }

    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                            throws IOException, ServletException {
        Map<?, ?> map = request.getParameterMap();
        final Map<String, String[]> newMap = new HashMap<String, String[]>();
        boolean format = false, isgfi = false;

        for ( Object keyO : map.keySet() ) {
            String key = (String) keyO;
            if ( key.equalsIgnoreCase( "format" ) ) {
                format = true;
            }

            if ( key.equalsIgnoreCase( "request" ) ) {
                String[] val = (String[]) map.get( key );
                if ( val != null && val.length > 0 && val[0].equalsIgnoreCase( "getfeatureinfo" ) ) {
                    isgfi = true;
                }
            }

            newMap.put( key, (String[]) map.get( key ) );
        }

        if ( !isgfi ) {
            chain.doFilter( request, response );
        }

        if ( !format ) {
            newMap.put( "format", new String[] { "image/png" } );
        }

        chain.doFilter( new HttpServletRequestWrapper( (HttpServletRequest) request ) {
            @Override
            public Map<?, ?> getParameterMap() {
                return newMap;
            }
        }, response );
    }

    public void init( FilterConfig conf )
                            throws ServletException {
        // no configuration is required
    }

}
