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
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPBody;
import javax.xml.namespace.QName;
import java.util.Iterator;


public class HeaderExample {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Argument required: " + "-Dsoap=<1.1|1.2>");
            System.exit(1);
        }

        String version = args[0];
        System.out.println("SOAP version is " + version);

        if (!(version.equals("1.1") || version.equals("1.2"))) {
            System.err.println("Value must be 1.1 or 1.2");
            System.exit(1);
        }

        try {
            // Create message factory
            MessageFactory messageFactory = null;

            if (version.equals("1.1")) {
                messageFactory = MessageFactory.newInstance();
            } else {
                messageFactory = MessageFactory.newInstance(
                            SOAPConstants.SOAP_1_2_PROTOCOL);
            }

            // Create a message
            SOAPMessage message = messageFactory.createMessage();

            // Get the SOAP header from the message and 
            //  add headers to it
            SOAPHeader header = message.getSOAPHeader();

            String nameSpace = "ns";
            String nameSpaceURI = "http://gizmos.com/NSURI";

            QName order = new QName(nameSpaceURI, "orderDesk", nameSpace);
            SOAPHeaderElement orderHeader = header.addHeaderElement(order);

            if (version.equals("1.1")) {
                orderHeader.setActor("http://gizmos.com/orders");
            } else {
                orderHeader.setRole("http://gizmos.com/orders");
            }

            QName shipping = new QName(nameSpaceURI, "shippingDesk", nameSpace);
            SOAPHeaderElement shippingHeader = header.addHeaderElement(
                        shipping);

            if (version.equals("1.1")) {
                shippingHeader.setActor("http://gizmos.com/shipping");
            } else {
                shippingHeader.setRole("http://gizmos.com/shipping");
            }

            QName confirmation = new QName(
                        nameSpaceURI,
                        "confirmationDesk",
                        nameSpace);
            SOAPHeaderElement confirmationHeader = header.addHeaderElement(
                        confirmation);

            if (version.equals("1.1")) {
                confirmationHeader.setActor("http://gizmos.com/confirmations");
            } else {
                confirmationHeader.setRole("http://gizmos.com/confirmations");
            }

            // Add mustUnderstand attribute to this header element
            confirmationHeader.setMustUnderstand(true);

            QName billing = new QName(nameSpaceURI, "billingDesk", nameSpace);
            SOAPHeaderElement billingHeader = header.addHeaderElement(billing);

            if (version.equals("1.1")) {
                billingHeader.setActor("http://gizmos.com/billing");
            } else {
                billingHeader.setRole("http://gizmos.com/billing");
            }

            // Add relay attribute to this header element, if SOAP 1.2
            if (version.equals("1.2")) {
                billingHeader.setRelay(true);
            }

            // Get the SOAP body from the message but leave
            // it empty
            SOAPBody body = message.getSOAPBody();

            message.saveChanges();

            // Display the message that would be sent
            System.out.println("\n----- Request Message ----\n");
            message.writeTo(System.out);
            System.out.println();

            // Look at the headers
            Iterator allHeaders = header.examineAllHeaderElements();

            while (allHeaders.hasNext()) {
                SOAPHeaderElement headerElement = (SOAPHeaderElement) allHeaders
                    .next();
                QName headerName = headerElement.getElementQName();
                System.out.println("\nHeader name is " + headerName.toString());

                if (version.equals("1.1")) {
                    System.out.println("Actor is " + headerElement.getActor());
                } else {
                    System.out.println("Role is " + headerElement.getRole());
                }

                System.out.println(
                        "mustUnderstand is "
                        + headerElement.getMustUnderstand());

                if (version.equals("1.2")) {
                    System.out.println("relay is " + headerElement.getRelay());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
