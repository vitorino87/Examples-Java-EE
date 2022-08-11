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


import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import static javax.xml.soap.SOAPConstants.URI_NS_SOAP_ENVELOPE;
import static javax.xml.soap.SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;
import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.Locale;


public class SOAPFaultTest {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Argument required: " + "-Dsoap=<1.1|1.2>");
            System.exit(1);
        }

        String version = args[0];

        if (!(version.equals("1.1") || version.equals("1.2"))) {
            System.err.println("Value must be 1.1 or 1.2");
            System.exit(1);
        }

        try {
            MessageFactory messageFactory = null;

            if (version.equals("1.1")) {
                messageFactory = MessageFactory.newInstance();
            } else {
                messageFactory = MessageFactory.newInstance(
                            SOAPConstants.SOAP_1_2_PROTOCOL);
            }

            SOAPMessage message = messageFactory.createMessage();
            SOAPBody body = message.getSOAPBody();
            SOAPFault fault = body.addFault();

            if (version.equals("1.1")) {
                QName faultName = new QName(
                            SOAPConstants.URI_NS_SOAP_ENVELOPE,
                            "Client");
                fault.setFaultCode(faultName);
            } else {
                QName faultName = new QName(
                            SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE,
                            "Sender");
                fault.setFaultCode(faultName);
            }

            if (version.equals("1.1")) {
                fault.setFaultString("Message does not have necessary info");
                fault.setFaultActor("http://gizmos.com/order");
            } else {
                fault.addFaultReasonText(
                        "Message does not have necessary info",
                        Locale.US);
                fault.setFaultRole("http://gizmos.com/order");
            }

            Detail detail = fault.addDetail();

            QName entryName = new QName(
                        "http://gizmos.com/orders/",
                        "order",
                        "PO");
            DetailEntry entry = detail.addDetailEntry(entryName);
            entry.addTextNode("Quantity element does not have a value");

            QName entryName2 = new QName(
                        "http://gizmos.com/confirm",
                        "confirmation",
                        "PO");
            DetailEntry entry2 = detail.addDetailEntry(entryName2);
            entry2.addTextNode("Incomplete address: no zip code");

            message.saveChanges();

            System.out.println("Here is what the XML message looks like:\n");
            message.writeTo(System.out);
            System.out.println();
            System.out.println();

            // Now retrieve the SOAPFault object and
            // its contents, after checking to see that 
            // there is one
            if (body.hasFault()) {
                SOAPFault newFault = body.getFault();

                // Get the qualified name of the fault code
                QName code = newFault.getFaultCodeAsQName();

                System.out.println("SOAP fault contains: ");
                System.out.println(" Fault code = " + code.toString());
                System.out.println(" Local name = " + code.getLocalPart());
                System.out.println(
                        " Namespace prefix = " + code.getPrefix()
                        + ", bound to " + code.getNamespaceURI());

                if (version.equals("1.1")) {
                    String string = newFault.getFaultString();
                    System.out.println(" Fault string = " + string);

                    String actor = newFault.getFaultActor();

                    if (actor != null) {
                        System.out.println(" Fault actor = " + actor);
                    }
                } else {
                    String reasonText = newFault.getFaultReasonText(Locale.US);
                    System.out.println(" Fault reason text = " + reasonText);

                    String role = newFault.getFaultRole();

                    if (role != null) {
                        System.out.println(" Fault role = " + role);
                    }
                }

                Detail newDetail = newFault.getDetail();

                if (newDetail != null) {
                    Iterator entries = newDetail.getDetailEntries();

                    while (entries.hasNext()) {
                        DetailEntry newEntry = (DetailEntry) entries.next();
                        String value = newEntry.getValue();
                        System.out.println(" Detail entry = " + value);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
