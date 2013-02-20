//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/BasicIdentification.java $
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

import java.util.ArrayList;
import java.util.List;

import org.deegree.framework.util.Pair;

/**
 * <code>BasicIdentification</code> is a bean representation of a basicIdentification type, which
 * can be used for identifying and describing a set of data. Defined in ows 1.1.0.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10830 $, $Date: 2008-03-31 11:33:56 +0200 (Mon, 31 Mar 2008) $
 * 
 */
public class BasicIdentification extends DescriptionBase {

    private final Pair<String, String> identifier;

    private final List<Pair<String, String>> metadatas;

    /**
     * @param keywords
     * @param abstracts
     * @param title
     * @param identifier
     *            a &lt; value, codeSpace &gt; pair
     * @param metadatas
     *            a list of &lt xlink:href, about &gt; pairs.
     */
    public BasicIdentification( List<String> title, List<String> abstracts, List<Keywords> keywords,
                                Pair<String, String> identifier, List<Pair<String, String>> metadatas ) {
        super( title, abstracts, keywords );
        this.identifier = identifier;
        if ( metadatas == null ) {
            metadatas = new ArrayList<Pair<String, String>>();
        }
        this.metadatas = metadatas;
    }

    /**
     * @return the identifier &lt; value, codeSpace &gt; pair, may be <code>null</code>
     */
    public final Pair<String, String> getIdentifier() {
        return identifier;
    }

    /**
     * @return the metadatas a list of &lt xlink:href, about &gt; pairs may be empty but never
     *         <code>null</code>
     */
    public final List<Pair<String, String>> getMetadatas() {
        return metadatas;
    }

}
