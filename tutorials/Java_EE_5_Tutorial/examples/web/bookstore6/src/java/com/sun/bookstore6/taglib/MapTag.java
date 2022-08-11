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


package com.sun.bookstore6.taglib;

import com.sun.bookstore6.components.MapComponent;
import javax.faces.component.UIComponent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.webapp.UIComponentELTag;


/**
 * <p>{@link UIComponentTag} for an image map.</p>
 */
public class MapTag extends UIComponentELTag {
    private javax.el.MethodExpression action = null;
    private javax.el.MethodExpression actionListener = null;
    private javax.el.ValueExpression current = null;
    private javax.el.ValueExpression immediate = null;
    private javax.el.ValueExpression styleClass = null;

    public void setCurrent(javax.el.ValueExpression current) {
        this.current = current;
    }

    public void setActionListener(javax.el.MethodExpression actionListener) {
        this.actionListener = actionListener;
    }

    public void setAction(javax.el.MethodExpression action) {
        this.action = action;
    }

    public void setImmediate(javax.el.ValueExpression immediate) {
        this.immediate = immediate;
    }

    public void setStyleClass(javax.el.ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    public String getComponentType() {
        return ("DemoMap");
    }

    public String getRendererType() {
        return ("DemoMap");
    }

    public void release() {
        super.release();
        current = null;
        styleClass = null;
        actionListener = null;
        action = null;
        immediate = null;
        styleClass = null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        MapComponent map = (MapComponent) component;

        if (styleClass != null) {
            if (!styleClass.isLiteralText()) {
                map.setValueExpression("styleClass", styleClass);
            } else {
                map.getAttributes()
                   .put(
                    "styleClass",
                    styleClass.getExpressionString());
            }
        }

        if (actionListener != null) {
            map.addActionListener(
                    new MethodExpressionActionListener(actionListener));
        }

        if (action != null) {
            map.setActionExpression(action);
        }

        if (immediate != null) {
            if (!immediate.isLiteralText()) {
                map.setValueExpression("immediate", immediate);
            } else {
                map.setImmediate(
                        new Boolean(immediate.getExpressionString())
                        .booleanValue());
            }
        }
    }
}
