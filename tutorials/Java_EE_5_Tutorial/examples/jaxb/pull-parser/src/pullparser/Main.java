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


import java.io.FileInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import javax.xml.stream.*;
import static javax.xml.stream.XMLStreamConstants.*;
import contact.Contact;


/*
 * Use is subject to the license terms.
 */

/**
 *
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String nameToLookFor = args[0];

        JAXBContext jaxbContext = JAXBContext.newInstance("contact");
        Unmarshaller um = jaxbContext.createUnmarshaller();

        // set up a parser
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmlr = xmlif.createXMLStreamReader(
                    new FileReader("contact.xml"));

        // move to the root element and check its name.
        xmlr.nextTag();
        xmlr.require(START_ELEMENT, null, "addressBook");

        xmlr.nextTag(); // move to the first <contact> element.

        while (xmlr.getEventType() == START_ELEMENT) {
            // unmarshall one <contact> element into a JAXB Contact object
            xmlr.require(START_ELEMENT, null, "contact");

            Contact contact = (Contact) um.unmarshal(xmlr);

            if (contact.getName()
                           .equals(nameToLookFor)) {
                // we found what we wanted to find. show it and quit now.
                System.out.println(
                        "the e-mail address is " + contact.getEmail());

                return;
            }

            if (xmlr.getEventType() == CHARACTERS) {
                xmlr.next(); // skip the whitespace between <contact>s.
            }
        }

        System.out.println("Unable to find " + nameToLookFor);
    }
}
