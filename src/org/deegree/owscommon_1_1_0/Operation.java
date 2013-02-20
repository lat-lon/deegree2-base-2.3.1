//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/Operation.java $
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

package org.deegree.owscommon_1_1_0;

import java.util.List;

import org.deegree.framework.util.Pair;

/**
 * <code>Operation</code> encapsulation of the operationMetadat/operation of ows 1.0.0.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10830 $, $Date: 2008-03-31 11:33:56 +0200 (Mon, 31 Mar 2008) $
 * 
 */

public class Operation {

    private final List<Pair<String, List<DomainType>>> getURLs;

    private final List<Pair<String, List<DomainType>>> postURLs;

    private final List<DomainType> constraints;

    private final List<DomainType> parameters;

    private final List<Pair<String, String>> metadataAttribs;

    private final String name;

    /**
     * @param getURLs
     *            and their constraints
     * @param postURLs
     *            and their constraints
     * @param parameters
     * @param constraints
     * @param metadataAttribs
     *            list containing &lt;xlink:href,about&gt; pairs.
     * @param name
     */
    public Operation( List<Pair<String, List<DomainType>>> getURLs, List<Pair<String, List<DomainType>>> postURLs,
                      List<DomainType> parameters, List<DomainType> constraints,
                      List<Pair<String, String>> metadataAttribs, String name ) {
        this.getURLs = getURLs;
        this.postURLs = postURLs;
        this.parameters = parameters;
        this.constraints = constraints;
        this.metadataAttribs = metadataAttribs;
        this.name = name;
    }

    /**
     * @return the getURLs.
     */
    public final List<Pair<String, List<DomainType>>> getGetURLs() {
        return getURLs;
    }

    /**
     * @return the postURLs.
     */
    public final List<Pair<String, List<DomainType>>> getPostURLs() {
        return postURLs;
    }

    /**
     * @return the constraints.
     */
    public final List<DomainType> getConstraints() {
        return constraints;
    }

    /**
     * @return the parameters.
     */
    public final List<DomainType> getParameters() {
        return parameters;
    }

    /**
     * @return the metadataAttribs &lt;xlink:href,about&gt; or either may be <code>null</code>.
     */
    public final List<Pair<String, String>> getMetadataAttribs() {
        return metadataAttribs;
    }

    /**
     * @return the name.
     */
    public final String getName() {
        return name;
    }

}