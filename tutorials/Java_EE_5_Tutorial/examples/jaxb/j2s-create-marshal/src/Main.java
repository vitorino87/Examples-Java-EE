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


import java.io.File;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import cardfile.BusinessCard;
import cardfile.Address;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.bind.ValidationEventLocator;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;


public class Main {
    public static void main(String[] args) throws Exception {
        final File f = new File("src/bcard.xml");

        // Illustrate two methods to create JAXBContext for j2s binding.
        // (1) by root classes newInstance(Class ...)
        JAXBContext context1 = JAXBContext.newInstance(BusinessCard.class);

        // (2) by package, requires jaxb.index file in package cardfile.
        //     newInstance(String packageNames)
        JAXBContext context2 = JAXBContext.newInstance("cardfile");

        Marshaller m = context1.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(
            getCard(),
            System.out);

        // illustrate optional unmarshal validation.
        Marshaller m2 = context1.createMarshaller();
        m2.marshal(
            getCard(),
            new FileOutputStream(f));

        Unmarshaller um = context2.createUnmarshaller();
        um.setSchema(getSchema("schema1.xsd"));

        Object bce = um.unmarshal(f);
        m.marshal(bce, System.out);
    }

    /** returns a JAXP 1.3 schema by parsing the specified resource. */
    static Schema getSchema(String schemaResourceName)
        throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);

        try {
            return sf.newSchema(Main.class.getResource(schemaResourceName));
        } catch (SAXException se) {
            // this can only happen if there's a deployment error and the resource is missing.
            throw se;
        }
    }

    private static BusinessCard getCard() {
        return new BusinessCard(
                "John Doe",
                "Sr. Widget Designer",
                "Acme, Inc.",
                new Address(
                        null,
                        "123 Widget Way",
                        "Anytown",
                        "MA",
                        (short) 12345),
                "123.456.7890",
                null,
                "123.456.7891",
                "John.Doe@Acme.ORG");
    }
}
