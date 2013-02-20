//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/enterprise/servlet/MockHttpServletResponse.java $
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
package org.deegree.enterprise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
class MockHttpServletResponse implements HttpServletResponse {
    private Writer myWriter;

    public MockHttpServletResponse(Writer writer) {
        myWriter = writer;
    }

    public void addCookie(Cookie arg0) {
        // TODO Auto-generated method stub

    }

    public boolean containsHeader(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public String encodeURL(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String encodeRedirectURL(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String encodeUrl(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String encodeRedirectUrl(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void sendError(int arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub

    }

    public void sendError(int arg0) throws IOException {
        // TODO Auto-generated method stub

    }

    public void sendRedirect(String arg0) throws IOException {
        // TODO Auto-generated method stub

    }

    public void setDateHeader(String arg0, long arg1) {
        // TODO Auto-generated method stub

    }

    public void addDateHeader(String arg0, long arg1) {
        // TODO Auto-generated method stub

    }

    public void setHeader(String arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    public void addHeader(String arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    public void setIntHeader(String arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    public void addIntHeader(String arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    public void setStatus(int arg0) {
        // TODO Auto-generated method stub

    }

    public void setStatus(int arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    public String getCharacterEncoding() {
        return "utf-8";
    }

    public ServletOutputStream getOutputStream() throws IOException {
        ServletOutputStream out = new ServletOutputStream() {

            public void write(int b) throws IOException {
                myWriter.write(b);

            }
        };
        return out;
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(myWriter);
    }

    public void setContentLength(int arg0) {
        // TODO Auto-generated method stub

    }

    public void setContentType(String arg0) {
        // TODO Auto-generated method stub

    }

    public void setBufferSize(int arg0) {
        // TODO Auto-generated method stub

    }

    public int getBufferSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void flushBuffer() throws IOException {
        // TODO Auto-generated method stub

    }

    public void resetBuffer() {
        // TODO Auto-generated method stub

    }

    public boolean isCommitted() {
        // TODO Auto-generated method stub
        return false;
    }

    public void reset() {
        // TODO Auto-generated method stub

    }

    public void setLocale(Locale arg0) {
        // TODO Auto-generated method stub

    }

    public Locale getLocale() {
        // TODO Auto-generated method stub
        return Locale.getDefault();
    }
    
    
    public String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setCharacterEncoding( String arg0 ) {
        // TODO Auto-generated method stub

    }
};
