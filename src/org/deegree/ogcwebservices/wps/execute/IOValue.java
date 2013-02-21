//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/execute/IOValue.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
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
 Aennchenstraße 19
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
package org.deegree.ogcwebservices.wps.execute;

import java.net.URI;
import java.net.URL;

import org.deegree.datatypes.Code;
import org.deegree.datatypes.values.TypedLiteral;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.ogcwebservices.wps.WPSDescription;

/**
 * IOValue.java
 * 
 * Created on 24.03.2006. 16:33:24h
 * 
 * Value of one input to a process or one output from a process.
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * @author last edited by: $Author: rbezema $
 *
 * @version $Revision: 11054 $, $Date: 2008-04-14 09:47:40 +0200 (Mo, 14. Apr 2008) $
 */
public class IOValue extends WPSDescription {

    /**
     * Identifies this input or output value as a web accessible resource, and references that
     * resource. For an input, this element may be used by a client for any process input coded as
     * ComplexData in the ProcessDescription. For an output, this element shall be used by a server
     * when "store" in the Execute request is "true".
     */

    private ComplexValueReference complexValueReference;

    /**
     * Identifies this input or output value as a complex value data structure encoded in XML (e.g.,
     * using GML), and provides that complex value data structure. For an input, this element may be
     * used by a client for any process input coded as ComplexData in the ProcessDescription. For an
     * output, this element shall be used by a server when "store" in the Execute request is
     * "false".
     */
    private ComplexValue complexValue;

    /**
     * Identifies this input or output value as a literal value of a simple quantity (e.g., one
     * number), and provides that value.
     */
    private TypedLiteral literalValue;

    /**
     * Identifies this input or output value as an ows:BoundingBox data structure, and provides that
     * ows:BoundingBox data structure.
     */
    private Envelope boundingBoxValue;

    /**
     * 
     * @param identifier
     * @param title
     * @param _abstract
     * @param boundingBoxValue
     * @param complexValue
     * @param complexValueReference
     * @param literalValue
     */
    public IOValue( Code identifier, String title, String _abstract, Envelope boundingBoxValue,
                    ComplexValue complexValue, ComplexValueReference complexValueReference,
                    TypedLiteral literalValue ) {
        super( identifier, title, _abstract );
        this.boundingBoxValue = boundingBoxValue;
        this.complexValue = complexValue;
        this.complexValueReference = complexValueReference;
        this.literalValue = literalValue;
    }

    /**
     * 
     * @return complexValueReference
     */
    public ComplexValueReference getComplexValueReference() {
        return complexValueReference;
    }

    /**
     * 
     * @param value
     */
    public void setComplexValueReference( ComplexValueReference value ) {
        this.complexValueReference = value;
    }

    /**
     * 
     * @return complexValue
     */
    public ComplexValue getComplexValue() {
        return complexValue;
    }

    /**
     * 
     * @param value
     */
    public void setComplexValue( ComplexValue value ) {
        this.complexValue = value;
    }

    /**
     * 
     * @return literalValue
     */
    public TypedLiteral getLiteralValue() {
        return literalValue;
    }

    /**
     * 
     * @param value
     */
    public void setLiteralValue( TypedLiteral value ) {
        this.literalValue = value;
    }

    /**
     * 
     * @return boundingBoxValue
     */
    public Envelope getBoundingBoxValue() {
        return boundingBoxValue;
    }

    /**
     * 
     * @return boundingBoxValueType
     */
    public boolean isBoundingBoxValueType() {
        boolean boundingBoxValueType = false;
        if ( null != boundingBoxValue ) {
            boundingBoxValueType = true;
        }
        return boundingBoxValueType;
    }

    /**
     * 
     * @return complexValueReferenceType
     */
    public boolean isComplexValueReferenceType() {
        boolean complexValueReferenceType = false;
        if ( null != complexValueReference ) {
            complexValueReferenceType = true;
        }
        return complexValueReferenceType;
    }

    /**
     * 
     * @return true if the value is complex
     */
    public boolean isComplexValueType() {
        boolean complexValueType = false;
        if ( null != complexValue ) {
            complexValueType = true;
        }
        return complexValueType;
    }

    /**
     * @return true if the value is a literal
     */
    public boolean isLiteralValueType() {
        boolean literalValueType = false;
        if ( null != literalValue ) {
            literalValueType = true;
        }
        return literalValueType;
    }

    /**
     * 
     * @param value
     */
    public void setBoundingBoxValue( Envelope value ) {
        this.boundingBoxValue = value;
    }

    /**
     * <code>ComplexValueReference</code> is simple wrapper class.
     *
     * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
     *
     * @author last edited by: $Author: rbezema $
     *
     * @version $Revision: 11054 $, $Date: 2008-04-14 09:47:40 +0200 (Mo, 14. Apr 2008) $
     *
     */
    public static class ComplexValueReference extends ComplexValueEncoding {

        protected URL reference;

        /**
         * @param encoding
         * @param format
         * @param schema
         * @param reference 
         */
        public ComplexValueReference( String format, URI encoding, URL schema, URL reference ) {
            super( format, encoding, schema );
            this.reference = reference;
        }

        /**
         * @return the reference to the data
         */
        public URL getReference() {
            return reference;
        }

    }

}
