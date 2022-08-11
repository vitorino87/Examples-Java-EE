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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;


/**
 * <p>The JSP <code>Tag</code> associated with the FormBasedLoginComponent.</p>
 */
public class FormBasedLoginTag extends UIComponentELTag {
    private ValueExpression buttonLabel;
    private ValueExpression passwordLabel;

    /**
     * Holds value of property resetButtonLabel.
     */
    private ValueExpression resetButtonLabel;
    private ValueExpression userNameLabel;

    public void setUserNameLabel(javax.el.ValueExpression userNameLabel) {
        this.userNameLabel = userNameLabel;
    }

    /**
     * Getter for property usernameLabel.
     * @return Value of property usernameLabel.
     */
    public ValueExpression getUserNameLabel() {
        return this.userNameLabel;
    }

    public void setPasswordLabel(ValueExpression passwordLabel) {
        this.passwordLabel = passwordLabel;
    }

    /**
     * Getter for property passwordLabel.
     * @return Value of property passwordLabel.
     */
    public ValueExpression getPasswordLabel() {
        return this.passwordLabel;
    }

    public void setButtonLabel(ValueExpression buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    /**
     * Getter for property buttonLabel.
     * @return Value of property buttonLabel.
     */
    public ValueExpression getButtonLabel() {
        return this.buttonLabel;
    }

    /**
     * Getter for property resetButtonLabel.
     * @return Value of property resetButtonLabel.
     */
    public ValueExpression getResetButtonLabel() {
        return this.resetButtonLabel;
    }

    /**
     * Setter for property resetButtonLabel.
     * @param resetButtonLabel New value of property resetButtonLabel.
     */
    public void setResetButtonLabel(ValueExpression resetButtonLabel) {
        this.resetButtonLabel = resetButtonLabel;
    }

    public String getComponentType() {
        return "bank.FormBasedLogin";
    }

    public String getRendererType() {
        return null;
    }

    public void release() {
        super.release();
        this.userNameLabel = null;
        this.passwordLabel = null;
        this.buttonLabel = null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        FormBasedLoginComponent fblc = (FormBasedLoginComponent) component;

        if (userNameLabel != null) {
            if (!userNameLabel.isLiteralText()) {
                fblc.setValueExpression("userNameLabel", userNameLabel);
            } else {
                fblc.setUsernameLabel(userNameLabel.getExpressionString());
            }
        }

        if (passwordLabel != null) {
            if (!passwordLabel.isLiteralText()) {
                fblc.setValueExpression("passwordLabel", passwordLabel);
            } else {
                fblc.setPasswordLabel(passwordLabel.getExpressionString());
            }
        }

        if (buttonLabel != null) {
            if (!buttonLabel.isLiteralText()) {
                fblc.setValueExpression("buttonLabel", buttonLabel);
            } else {
                fblc.setButtonLabel(buttonLabel.getExpressionString());
            }
        }

        if (resetButtonLabel != null) {
            if (!resetButtonLabel.isLiteralText()) {
                fblc.setValueExpression("resetButtonLabel", resetButtonLabel);
            } else {
                fblc.setResetButtonLabel(
                        resetButtonLabel.getExpressionString());
            }
        }
    }
}
