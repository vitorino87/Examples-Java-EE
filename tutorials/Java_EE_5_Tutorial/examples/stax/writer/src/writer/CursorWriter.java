/*
 * Copyright (c) 2006 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */


package writer;

import java.io.FileWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.util.Date;
import javax.xml.namespace.QName;


/**
 * CursorWriter sample is used to demonstrate the use of STAX Writer api's.
 *
 * @author k.venugopal@sun.com
 */
public class CursorWriter {
    private static String filename = null;

    private static void printUsage() {
        System.out.println(
                "java -classpath <.....> CursorWriter -f <outputFileName>");
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {
            String fileName = null;

            try {
                if (args[0].equals("-f")) {
                    fileName = args[1];
                } else {
                    printUsage();

                    return;
                }
            } catch (Exception ex) {
                printUsage();

                return;
            }

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xtw = null;
            xtw = xof.createXMLStreamWriter(new FileWriter(fileName));
            xtw.writeComment(
                    "all elements here are explicitly in the HTML namespace");
            xtw.writeStartDocument("utf-8", "1.0");
            xtw.setPrefix("html", "http://www.w3.org/TR/REC-html40");
            xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "html");
            xtw.writeNamespace("html", "http://www.w3.org/TR/REC-html40");
            xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "head");
            xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "title");
            xtw.writeCharacters("Frobnostication");
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "body");
            xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "p");
            xtw.writeCharacters("Moved to");
            xtw.writeStartElement("http://www.w3.org/TR/REC-html40", "a");
            xtw.writeAttribute("href", "http://frob.com");
            xtw.writeCharacters("here");
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeEndDocument();
            xtw.flush();
            xtw.close();
        } catch (Exception ex) {
            System.err.println(
                    "Exception occurred while running writer samples");
        }

        System.out.println("Done");
    }
}
