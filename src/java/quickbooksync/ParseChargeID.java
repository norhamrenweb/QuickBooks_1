/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickbooksync;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.xml.sax.InputSource;


/**
 *
 * @author nmohamed
 */
public class ParseChargeID {
    static Logger log = Logger.getLogger(ParseChargeID.class.getName());
    
    /**
     *
     * @param args
     * @return
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public String parsechargeid(String args) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {

DOMParser parser = new DOMParser();
if(args == null)
{
return null;    

}
else
{
    parser.parse(new InputSource(new java.io.StringReader(args)));
    Document doc = parser.getDocument();
    String message = doc.getDocumentElement().getTextContent();
    String value = message.substring(8);
    return value;
}
    
    }
    public String parsepaymentid(String args) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, SAXException, IOException {

DOMParser parser = new DOMParser();
if(args == null)
{
return null;    

}
else
{
    parser.parse(new InputSource(new java.io.StringReader(args)));
    Document doc = parser.getDocument();
    String message = doc.getDocumentElement().getTextContent();
    String value = message.substring(9);
    return value;
}
    
    }
}
