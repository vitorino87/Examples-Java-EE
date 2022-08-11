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

import javax.el.ELResolver;
import javax.el.ELContext;
import javax.el.PropertyNotFoundException;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Collections;


/**
 *  An <code>ELResolver</code> implementation to detect instances
 * of <code>EnumManagedBean</code> and invoke the <code>getEnum()</code>
 * method.
 */
public class EnumResolver extends ELResolver {
    public Object getValue(
        ELContext elContext,
        Object base,
        Object property) {
        if (((base != null) && (property != null))
                && base instanceof EnumManagedBean) {
            elContext.setPropertyResolved(true);

            return ((EnumManagedBean) base).getEnum(property.toString());
        }

        return null;
    }

    public Class<?> getCommonPropertyType(
        ELContext elContext,
        Object base) {
        return EnumManagedBean.class;
    }

    public Class<?> getType(
        ELContext elContext,
        Object base,
        Object property) {
        if (((base != null) && (property != null))
                && base instanceof EnumManagedBean) {
            elContext.setPropertyResolved(true);
            throw new PropertyNotFoundException();
        }

        return null;
    }

    public void setValue(
        ELContext elContext,
        Object base,
        Object property,
        Object value) {
        if (((base != null) && (property != null))
                && base instanceof EnumManagedBean) {
            elContext.setPropertyResolved(true);
            throw new PropertyNotFoundException(); // readonly
        }
    }

    public boolean isReadOnly(
        ELContext elContext,
        Object base,
        Object property) {
        if (((base != null) && (property != null))
                && base instanceof EnumManagedBean) {
            elContext.setPropertyResolved(true);

            return true; // readonly
        }

        return false;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(
        ELContext elContext,
        Object base) {
        return Collections.<FeatureDescriptor>emptyList()
                          .iterator();
    }
}
