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
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.AttachmentPart;
import java.net.URL;
import java.util.Iterator;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.activation.DataHandler;


public class Attachments {
    public static void main(String[] args) {
        FileReader fr = null;
        BufferedReader br = null;
        String line = "";

        try {
            // Create message factory
            MessageFactory messageFactory = MessageFactory
                .newInstance();

            // Create a message
            SOAPMessage message = messageFactory.createMessage();

            // Get the SOAP header and body from the message
            // and remove the header
            SOAPHeader header = message.getSOAPHeader();
            SOAPBody body = message.getSOAPBody();
            header.detachNode();

            // Create attachment part for text
            AttachmentPart attachment1 = message.createAttachmentPart();

            fr = new FileReader(new File(args[0]));
            br = new BufferedReader(fr);

            String stringContent = "";
            line = br.readLine();

            while (line != null) {
                stringContent = stringContent.concat(line);
                stringContent = stringContent.concat("\n");
                line = br.readLine();
            }

            attachment1.setContent(stringContent, "text/plain");
            attachment1.setContentId("attached_text");

            message.addAttachmentPart(attachment1);

            // Create attachment part for image
            URL url = new URL("file:///../xml-pic.jpg");
            DataHandler dataHandler = new DataHandler(url);
            AttachmentPart attachment2 = message.createAttachmentPart(
                        dataHandler);
            attachment2.setContentId("attached_image");

            message.addAttachmentPart(attachment2);

            // Now extract the attachments
            Iterator iterator = message.getAttachments();

            while (iterator.hasNext()) {
                AttachmentPart attached = (AttachmentPart) iterator.next();
                String id = attached.getContentId();
                String type = attached.getContentType();
                System.out.println(
                        "Attachment " + id + " has content type " + type);

                if (type.equals("text/plain")) {
                    Object content = attached.getContent();
                    System.out.println("Attachment contains:\n" + content);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.toString());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("I/O exception: " + e.toString());
            System.exit(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
