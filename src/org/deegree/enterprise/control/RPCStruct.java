//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/control/RPCStruct.java $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The class encapsulates a RPC struct.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @version $Revision: 9338 $ $Date: 2007-12-27 13:31:31 +0100 (Thu, 27 Dec 2007) $
 */
public class RPCStruct {

    private HashMap<String, RPCMember> members = null;

    private List<RPCMember> mem = null;

    /**
     * 
     * 
     */
    public RPCStruct() {
        members = new HashMap<String, RPCMember>();
        mem = new ArrayList<RPCMember>();
    }

    /**
     * 
     * @param mem
     */
    public RPCStruct( RPCMember[] mem ) {
        members = new HashMap<String, RPCMember>( mem.length );
        this.mem = new ArrayList<RPCMember>( mem.length );
        for ( int i = 0; i < mem.length; i++ ) {
            members.put( mem[i].getName(), mem[i] );
            this.mem.add( mem[i] );
        }
    }

    /**
     * returns the members of the struct
     * 
     * @return members of the struct
     */
    public RPCMember[] getMembers() {
        RPCMember[] m = new RPCMember[members.size()];
        return mem.toArray( m );
    }

    /**
     * returns a named member of the struct. if no member with the passed name is contained within
     * the struct <tt>null</tt> will be returned.
     * 
     * @param name
     *            name of the struct member
     * 
     * @return struct member
     */
    public RPCMember getMember( String name ) {
        return members.get( name );
    }

    /**
     * adds a new member to the struct
     * 
     * @param member
     */
    public void addMember( RPCMember member ) {
        members.put( member.getName(), member );
    }

    /**
     * removes a member identified by its name from the struct
     * 
     * @param name
     * 
     * @return removed member
     */
    public RPCMember removeMember( String name ) {
        return members.remove( name );
    }

}
