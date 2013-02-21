//$HeadURL$
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

package org.deegree.model.filterencoding;

import java.util.List;

import org.deegree.model.feature.Feature;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class FunctionSoundex extends Function {

    /**
     * 
     */
    FunctionSoundex() {
        super();
    }

    /**
     * @param name
     * @param args
     */
    FunctionSoundex( String name, List<Expression> args ) {
        super( name, args );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.model.filterencoding.Function#evaluate(org.deegree.model.feature.Feature)
     */
    @Override
    public Object evaluate( Feature feature )
                            throws FilterEvaluationException {
        Literal literal = (Literal) args.get( 0 );
        String s = literal.getValue();

        return soundex( s );

    }

    private String soundex( String s ) {
        int SIZE = 4;

        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for ( int i = 0; i < x.length; i++ ) {
            switch ( x[i] ) {
            case 'B':
            case 'F':
            case 'P':
            case 'V': {
                x[i] = '1';
                break;
            }

            case 'C':
            case 'G':
            case 'J':
            case 'K':
            case 'Q':
            case 'S':
            case 'X':
            case 'Z': {
                x[i] = '2';
                break;
            }

            case 'D':
            case 'T': {
                x[i] = '3';
                break;
            }

            case 'L': {
                x[i] = '4';
                break;
            }

            case 'M':
            case 'N': {
                x[i] = '5';
                break;
            }

            case 'R': {
                x[i] = '6';
                break;
            }

            default: {
                x[i] = '0';
                break;
            }
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        char last = x[0];
        for ( int i = 1; i < x.length; i++ ) {
            if ( x[i] != '0' && x[i] != last ) {
                last = x[i];
                output += last;
            }
        }

        // pad with 0's or truncate
        for ( int i = output.length(); i < SIZE; i++ ) {
            output += '0';
        }
        output = output.substring( 0, SIZE );

        return output;
    }

}
