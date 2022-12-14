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

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;
import java.util.*;


public class ScreenTag extends SimpleTagSupport {
    private ArrayList<Parameter> parameters = null;
    private String screenId;

    public ScreenTag() {
        super();
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public void doTag() {
        parameters = new ArrayList<Parameter>();

        HashMap screens = (HashMap) ((DefinitionTag) getParent())
            .getScreens();

        if (screens != null) {
            try {
                if (!screens.containsKey(screenId)) {
                    screens.put(screenId, parameters);
                }

                getJspBody()
                    .invoke(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Debug.println("ScreenTag: Unable to get screens object.");
        }
    }
}
