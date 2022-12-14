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


package com.sun.bookstore3.template;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.util.*;


public class DefinitionTag extends SimpleTagSupport {
    private HashMap screens = null;
    private String definitionName = null;
    private String screenId;

    public DefinitionTag() {
        super();
    }

    public HashMap getScreens() {
        return screens;
    }

    public void setName(String name) {
        this.definitionName = name;
    }

    public void setScreen(String screenId) {
        this.screenId = screenId;
    }

    public void doTag() {
        try {
            screens = new HashMap();

            getJspBody()
                .invoke(null);

            Definition definition = new Definition();
            PageContext context = (PageContext) getJspContext();
            ArrayList params = (ArrayList) screens.get(screenId);
            Iterator ir = null;

            if (params != null) {
                ir = params.iterator();

                while (ir.hasNext()) {
                    definition.setParam((Parameter) ir.next());
                }

                // put the definition in the page context
                context.setAttribute(
                        definitionName,
                        definition,
                        context.APPLICATION_SCOPE);
            } else {
                Debug.println("DefinitionTag: params are not defined.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
