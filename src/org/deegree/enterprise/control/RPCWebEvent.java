//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/control/RPCWebEvent.java $
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
package org.deegree.enterprise.control;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */

public class RPCWebEvent extends WebEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private RPCMethodCall mc = null;

    /** Creates a new instance of RPCWebEvent */
    public RPCWebEvent( HttpServletRequest request ) {
        super( request );
    }

    /** Creates a new instance of RPCWebEvent */
    public RPCWebEvent( HttpServletRequest request, RPCMethodCall mc ) {
        super( request );
        this.mc = mc;
    }

    /** Creates a new instance of RPCWebEvent */
    public RPCWebEvent( FormEvent parent, RPCMethodCall mc ) {
        super( (HttpServletRequest) parent.getSource() );
        this.mc = mc;
    }

    /**
     * returns the the RPC methodcall extracted from the <tt>HttpServletRequest</tt> passed to the
     * first constructor.
     */
    public RPCMethodCall getRPCMethodCall() {
        if ( mc == null ) {
            try {
                mc = getMethodCall( (ServletRequest) this.getSource() );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        return mc;
    }

    /**
     * extracts the RPC method call from the
     * 
     * @param request
     * @throws RPCException
     */
    private RPCMethodCall getMethodCall( ServletRequest request )
                            throws RPCException {

        StringBuffer sb = new StringBuffer( 1000 );
        try {
            BufferedReader br = request.getReader();
            String line = null;
            while ( ( line = br.readLine() ) != null ) {
                sb.append( line );
            }
            br.close();
        } catch ( Exception e ) {
            throw new RPCException( "Error reading stream from servlet\n" + e.toString() );
        }

        String s = sb.toString();
        int pos1 = s.indexOf( "<methodCall>" );
        int pos2 = s.indexOf( "</methodCall>" );
        if ( pos1 < 0 ) {
            throw new RPCException( "request doesn't contain a RPC methodCall" );
        }
        s = s.substring( pos1, pos2 + 13 );

        StringReader reader = new StringReader( s );
        RPCMethodCall mc = RPCFactory.createRPCMethodCall( reader );

        return mc;
    }
}
