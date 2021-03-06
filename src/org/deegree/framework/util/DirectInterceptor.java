//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/util/DirectInterceptor.java $
//$Id: DirectInterceptor.java 11888 2008-05-26 08:41:56Z rbezema $
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

package org.deegree.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </A>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version 2.0, $Revision: 11888 $, $Date: 2008-05-26 10:41:56 +0200 (Mo, 26. Mai 2008) $
 * 
 * @see <a href="http://www.dofactory.com/patterns/PatternChain.aspx">Chain of Responsibility Design
 *      Pattern </a>
 * 
 * @since 2.0
 */
public class DirectInterceptor extends Interceptor {

    /**
     * to invoke
     */
    protected Object target;

    /**
     * @param target to intercept
     */
    public DirectInterceptor( Object target ) {
        this.target = target;
    }

    @Override
    protected Object handleInvocation( Method method, Object[] params )
                            throws IllegalAccessException, InvocationTargetException {
        return method.invoke( target, params );
    }

    @Override
    protected Object getTarget() {
        return target;
    }

}