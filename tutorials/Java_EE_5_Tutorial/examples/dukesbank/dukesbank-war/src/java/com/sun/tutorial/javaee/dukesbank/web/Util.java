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

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import com.sun.tutorial.javaee.dukesbank.util.Debug;


/**
 * A simple utilities class.
 */
public class Util {
    public enum Navigation {
        accountHist,
        accountList,
        atm,
        atmAck,
        error,
        logout,
        main,
        transferAck,
        transferFunds;
        public Object action() {
            return this;
        }
    }

    private static String RESOURCE_BASE_NAME = null;
    private static Map<Locale, ResourceBundle> RESOURCES = new HashMap<Locale, ResourceBundle>(
                2);

    // ------------------------------------------------------------ Constructors
    private Util() {
    }

    // ---------------------------------------------------------- Public Methods
    public static ResourceBundle getBundle(FacesContext context) {
        if (RESOURCE_BASE_NAME == null) {
            RESOURCE_BASE_NAME = context.getApplication()
                                        .getMessageBundle();
        }

        Locale locale = context.getViewRoot()
                               .getLocale();
        ResourceBundle bundle = RESOURCES.get(locale);

        if (bundle == null) {
            try {
                bundle = ResourceBundle.getBundle(RESOURCE_BASE_NAME, locale);
                RESOURCES.put(locale, bundle);
            } catch (Exception e) {
                Debug.print("Could not locate resource bundle.");
            }
        }

        return bundle;
    }

    public static String getString(
        FacesContext context,
        String key) {
        String text;

        try {
            text = getBundle(context)
                       .getString(key);
        } catch (Exception e) {
            text = "???" + key + "???";
        }

        return text;
    }

    public static void addMessage(
        FacesContext context,
        String clientId,
        String key,
        FacesMessage.Severity severity,
        Object... params) {
        // Look up the requested message text
        String text = getString(context, key);

        // Perform the requested substitutions
        if ((params != null) && (params.length > 0)) {
            text = MessageFormat.format(text, params);
        }

        // Construct and add a FacesMessage containing it
        context.addMessage(
            clientId,
            new FacesMessage(severity, text, text));

        // If debug is enabled, print the message to the log
        Debug.print(text);
    }

    public static void addErrorMessage(
        FacesContext context,
        String clientId,
        String key,
        Object... params) {
        addMessage(context, clientId, key, FacesMessage.SEVERITY_ERROR, params);
    }

    public static void addInfoMessage(
        FacesContext context,
        String clientId,
        String key,
        Object... params) {
        addMessage(context, clientId, key, FacesMessage.SEVERITY_INFO, params);
    }

    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance()
                           .getExternalContext();
    }
}
