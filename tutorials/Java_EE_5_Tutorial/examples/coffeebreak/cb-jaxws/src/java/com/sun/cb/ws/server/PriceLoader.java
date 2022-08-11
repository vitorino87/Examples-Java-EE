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


package com.sun.cb.ws.server;

import com.sun.cb.ws.server.PriceItemBean;
import java.math.BigDecimal;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


public final class PriceLoader {
    public static final PriceItemBean[] loadItems(String propsName) {
        ResourceBundle priceBundle = ResourceBundle.getBundle(propsName);

        Enumeration bundleKeys = priceBundle.getKeys();
        List<String> keyList = new ArrayList<String>();

        while (bundleKeys.hasMoreElements()) {
            String key = (String) bundleKeys.nextElement();
            String value = priceBundle.getString(key);
            keyList.add(value);
        }

        PriceItemBean[] items = (PriceItemBean[]) Array.newInstance(
                    PriceItemBean.class,
                    keyList.size());
        int k = 0;

        for (Iterator it = keyList.iterator(); it.hasNext();) {
            String s = (String) it.next();
            int commaIndex = s.indexOf(",");
            String name = s.substring(0, commaIndex)
                           .trim();
            String price = s.substring(
                    commaIndex + 1,
                    s.length())
                            .trim();
            items[k] = new PriceItemBean(
                    name,
                    new BigDecimal(price));
            k++;
        }

        return items;
    }
}
