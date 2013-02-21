package org.deegree.ogcwebservices.csw.iso_profile;
/* ----------------------------------------
   no header
   ---------------------------------------- */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.deegree.framework.util.IDGenerator;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.w3c.dom.Node;

/**
 *
 *
 * @version $Revision: 1.9 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: buesching $
 *
 * @version 1.0. $Revision: 1.9 $, $Date: 2008-01-25 14:36:09 $
 *
 * @since 2.0
 */
public class Mapping2_0_2 {

    private static IDGenerator idgen = IDGenerator.getInstance();

    private static Properties mapping = null;

    private Properties nsp = null;

    private static Properties namespaces = new Properties();

    private static Properties namespacesInverse = new Properties();

    static {
        mapping = new Properties();
        try {
            InputStream is = Mapping2_0_2.class.getResourceAsStream( "mapping2_0_2.properties" );
            InputStreamReader isr = new InputStreamReader( is );
            BufferedReader br = new BufferedReader( isr );
            String line = null;
            while ( ( line = br.readLine() ) != null ) {
                if ( !line.trim().startsWith( "#" ) ) {
                    String[] tmp = StringTools.toArray( line.trim(), "=", false );
                    if ( tmp[0].startsWith( "$namespace." ) ) {
                        String pre = tmp[0].substring( tmp[0].indexOf( '.' ) + 1, tmp[0].length() );
                        namespaces.put( pre, tmp[1] );
                        namespacesInverse.put( tmp[1], pre );
                    } else {
                        mapping.put( tmp[0], tmp[1] );
                    }
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private static NamespaceContext nsc = CommonNamespaces.getNamespaceContext();

    /**
     * maps a property name of GetRecords, Delete and Update request from the catalogue schema to
     * the underlying WFS schema
     *
     * @param node
     * @param nspDec
     * @return the value
     * @throws XMLParsingException
     */
    public String mapPropertyValue( Node node, String nspDec )
                            throws XMLParsingException {
        if ( nsp == null ) {
            createNsp( nspDec );
        }

        String s = XMLTools.getNodeAsString( node, ".", nsc, null );
        String prefix = s.substring( 0, s.indexOf( ':' ) );
        String localPre = namespacesInverse.getProperty( nsp.getProperty( prefix ) );

        String propertyValue = "./" + localPre + s.substring( s.indexOf( ':' ) );
        propertyValue = mapping.getProperty( propertyValue );

        return propertyValue;

    }

    private void createNsp( String nspDec ) {
        nsp = new Properties();
        String[] tmp = StringTools.toArray( nspDec, ";", false );
        for ( int i = 0; i < tmp.length; i++ ) {
            int p = tmp[i].indexOf( ':' );
            String pre = tmp[i].substring( 0, p );
            String val = tmp[i].substring( p + 1 );
            nsp.put( pre, val );
        }

    }

    /**
     * maps property names for sorting from the catalogue schema to the underlying WFS schema
     *
     * @param node
     * @param nspDec
     * @return the property
     * @throws XMLParsingException
     */
    public String mapSortProperty( Node node, String nspDec )
                            throws XMLParsingException {
        if ( nsp == null ) {
            createNsp( nspDec );
        }

        String s = XMLTools.getNodeAsString( node, ".", nsc, null );
        String prefix = s.substring( 0, s.indexOf( ':' ) );
        String localPre = namespacesInverse.getProperty( nsp.getProperty( prefix ) );

        String propertyValue = "./sortby/" + localPre + s.substring( s.indexOf( ':' ) );
        propertyValue = mapping.getProperty( propertyValue );

        return propertyValue;
    }

    /**
     * @param node
     * @param propName
     * @param wildCard
     * @return the literal
     * @throws XMLParsingException
     */
    public String getLiteralValueIsLike( Node node, String propName, String wildCard )
                            throws XMLParsingException {

        String literal = XMLTools.getNodeAsString( node, ".", nsc, null );
        String propertyName = propName.substring( propName.indexOf( ':' ) + 1 );

        String newLiteral = literal;
        if ( propertyName.equals( "subject" ) || propertyName.equals( "AlternateTitle" )
             || propertyName.equals( "ResourceIdentifier" ) || propertyName.equals( "ResourceLanguage" )
             || propertyName.equals( "AlternateTitle" ) || propertyName.equals( "ResourceIdentifier" )
             || propertyName.equals( "GeographicDescripionCode" ) || propertyName.equals( "TopicCategory" ) ) {
            newLiteral = StringTools.concat( 500, "%|", wildCard, literal, wildCard, "|%" );
        }

        return newLiteral;
    }

    /**
     * @param node
     * @return the value
     * @throws XMLParsingException
     */
    public String getLiteralValueIsEqualTo( Node node )
                            throws XMLParsingException {
        String literal = XMLTools.getNodeAsString( node, ".", nsc, null );
        return StringTools.concat( 100, "%|", literal, "|%" );
    }

    /**
     * @return a new generated id
     */
    public static String getId() {
        return "unknown" + Long.toString( idgen.generateUniqueID() );
    }

}