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


package com.sun.tutorial.javaee.dukesbank.web;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


public class FormBasedLoginComponent extends UIOutput {
    private static String defaultUsernameLabel = "Username:";
    private static String defaultPasswordLabel = "Password:";
    private static String defaultButtonLabel = "Login";
    private static String defaultResetButtonLabel = "Reset";
    private String buttonLabel;
    private String passwordLabel;
    private String resetButtonLabel;
    private String usernameLabel;

    public FormBasedLoginComponent() {
    }

    public String getFamily() {
        return "bank.FormBasedLogin";
    }

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("form", this);
        writer.writeAttribute("method", "post", null);
        writer.writeAttribute("action", "j_security_check", null);
        writer.startElement("table", this);
        writer.startElement("tr", this);
        writer.startElement("td", this);
        writer.startElement("label", this);
        writer.writeAttribute("for", "j_username", null);
        writer.writeText(
            this.getUsernameLabel(),
            "usernameLabel");
        writer.endElement("label");
        writer.endElement("td");

        writer.startElement("td", this);
        writer.startElement("input", this);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("name", "j_username", null);
        writer.writeAttribute("id", "j_username", null);
        writer.endElement("td");
        writer.endElement("tr");

        writer.startElement("tr", this);

        writer.startElement("td", this);
        writer.startElement("label", this);
        writer.writeAttribute("for", "j_password", null);
        writer.writeText(
            this.getPasswordLabel(),
            "passwordLabel");
        writer.endElement("label");
        writer.endElement("td");

        writer.startElement("td", this);
        writer.startElement("input", this);
        writer.writeAttribute("type", "password", null);
        writer.writeAttribute("name", "j_password", null);
        writer.endElement("td");

        writer.endElement("tr");

        writer.startElement("tr", this);
        writer.startElement("tr", this);
        writer.startElement("td", this);
        writer.startElement("input", this);
        writer.writeAttribute("type", "reset", null);
        writer.writeAttribute("name", "reset", null);
        writer.writeAttribute(
                "value",
                getResetButtonLabel(),
                "resetButtonLabel");
        writer.endElement("td");
        writer.startElement("td", this);
        writer.startElement("input", this);
        writer.writeAttribute("type", "submit", null);
        writer.writeAttribute("name", "submit", null);
        writer.writeAttribute(
            "value",
            getButtonLabel(),
            "buttonLabel");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");

        writer.endElement("form");
    }

    public void encodeChildren(FacesContext context) throws IOException {
    }

    public void encodeEnd(FacesContext context) throws IOException {
    }

    public String getUsernameLabel() {
        if (this.usernameLabel != null) {
            return this.usernameLabel;
        } else {
            return (defaultUsernameLabel);
        }
    }

    public void setUsernameLabel(String usernameLabel) {
        this.usernameLabel = usernameLabel;
    }

    public String getPasswordLabel() {
        if (this.passwordLabel != null) {
            return (this.passwordLabel);
        } else {
            return (defaultPasswordLabel);
        }
    }

    public void setPasswordLabel(String passwordLabel) {
        this.passwordLabel = passwordLabel;
    }

    public String getButtonLabel() {
        if (this.buttonLabel != null) {
            return (this.buttonLabel);
        } else {
            return (defaultButtonLabel);
        }
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    /**
     * Getter for property resetButtonLabel.
     *
     * @return Value of property resetButtonLabel.
     */
    public String getResetButtonLabel() {
        if (this.resetButtonLabel != null) {
            return (this.resetButtonLabel);
        } else {
            return (defaultResetButtonLabel);
        }
    }

    /**
     * Setter for property resetButtonLabel.
     *
     * @param resetButtonLabel New value of property resetButtonLabel.
     */
    public void setResetButtonLabel(String resetButtonLabel) {
        this.resetButtonLabel = resetButtonLabel;
    }
}
