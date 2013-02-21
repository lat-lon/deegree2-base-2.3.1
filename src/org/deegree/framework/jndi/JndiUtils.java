//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/jndi/JndiUtils.java $
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
package org.deegree.framework.jndi;

// J2EE 1.3

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * Utility class for retrieving home interfaces and environment values.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe</A>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 11766 $, $Date: 2008-05-19 10:18:44 +0200 (Mo, 19. Mai 2008) $
 */
public final class JndiUtils {

    /**
     * Default constructor is hidden to protect this class from unwanted instantiation.
     */
    private JndiUtils() {
        // nothing to do
    }

    /**
     * Lookup using <b>no</b> caching functionality.
     * 
     * @param name
     *            the environment name
     * @param classForNarrow
     *            the class to narrow
     * @return if an entry exists, an instance of the given class with a value retrieved from the JNDI tree otherwise
     *         <code>null</code>.
     * @throws NamingException
     */
    public static Object lookup( String name, Class<?> classForNarrow )
                            throws NamingException {
        return PortableRemoteObject.narrow( JndiUtils.getInitialContext().lookup( name ), classForNarrow );
    }

    /**
     * Lookup at Enterprise Java Beans environment value. For home interfaces use EJBRemoteFactory instead.
     * 
     * @param name
     *            the environment name
     * @param classForNarrow
     *            the class to narrow
     * @return if an entry exists, an instance of the given class with a value retrieved from the JNDI tree otherwise
     *         <code>null</code>.
     * @throws NamingException
     *             if the given lookup name is not in the JNDI tree
     */
    public static Object lookupEnv( String name, Class<?> classForNarrow )
                            throws NamingException {
        return lookup( "java:/comp/env/" + name, classForNarrow );
    }

    /**
     * Returns a list of naming entries for the given root node.
     * 
     * @param rootNode
     * @return list of naming entries
     * @throws NamingException
     *             when the given root node doesn't exists
     */
    public static NamingEnumeration<?> getNamingList( String rootNode )
                            throws NamingException {
        return JndiUtils.getInitialContext().listBindings( rootNode );
    }

    /**
     * Returns the initial context for this application.
     * 
     * @return the inital context for this appication. This depends on the system environment.
     * 
     * @throws NamingException
     *             if the context is not available
     */
    private static Context getInitialContext()
                            throws NamingException {
        return ( new InitialContext() );
    }
}