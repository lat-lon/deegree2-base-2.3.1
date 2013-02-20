//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/metadata/iso19115/ContactInfo.java $
/*
 ----------------    FILE HEADER  ------------------------------------------
 
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

package org.deegree.model.metadata.iso19115;

/**
 * Represents ContactInfo entity compliant to ISO 19115 schema.
 * 
 * 
 * @author <a href="mailto:schaefer@lat-lon.de">Axel Schaefer </a>
 * @version $Revision: 9343 $ $Date: 2007-12-27 14:30:32 +0100 (Thu, 27 Dec 2007) $
 * @see <a href="http://http://www.iso.ch">International Organization for Standardization</a>
 */
public class ContactInfo {

    private Address address = null;

    private String contactinstructions = null;

    private String hoursofservice = null;

    private OnlineResource onlineresource = null;

    private Phone phone = null;

    /**
     * Creates a new instance of ContactInfo
     * 
     * @param address
     * @param contactinstructions
     * @param hoursofservice
     * @param onlineresource
     * @param phone
     */
    public ContactInfo( Address address, String contactinstructions, String hoursofservice,
                        OnlineResource onlineresource, Phone phone ) {
        setAddress( address );
        setContactInstructions( contactinstructions );
        setHoursOfService( hoursofservice );
        setOnLineResource( onlineresource );
        setPhone( phone );
    }

    /**
     * @return address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @see ContactInfo#getAddress()
     */
    public void setAddress( Address address ) {
        this.address = address;
    }

    /**
     * @return contact instructions
     * 
     */
    public String getContactInstructions() {
        return contactinstructions;
    }

    /**
     * @see ContactInfo#getContactInstructions()
     */
    public void setContactInstructions( String contactinstructions ) {
        this.contactinstructions = contactinstructions;
    }

    /**
     * @return hours of service
     * 
     */
    public String getHoursOfService() {
        return hoursofservice;
    }

    /**
     * @see ContactInfo#getHoursOfService()
     */
    public void setHoursOfService( String hoursofservice ) {
        this.hoursofservice = hoursofservice;
    }

    /**
     * @return online resource
     * 
     */
    public OnlineResource getOnLineResource() {
        return onlineresource;
    }

    /**
     * @see ContactInfo#getOnLineResource()
     */
    public void setOnLineResource( OnlineResource onlineresource ) {
        this.onlineresource = onlineresource;
    }

    /**
     * @return phone
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * @see ContactInfo#getPhone()
     */
    public void setPhone( Phone phone ) {
        this.phone = phone;
    }

    /**
     * to String method
     */
    public String toString() {
        StringBuffer buf = new StringBuffer( 64 );
        buf.append( "address = " + address + "\n" );
        buf.append( "contactinstructions = " + contactinstructions + "\n" );
        buf.append( "hoursofservice = " + hoursofservice + "\n" );
        buf.append( "onlineresource = " + onlineresource + "\n" );
        buf.append( "phone = " + phone + "\n" );
        return buf.toString();
    }

}