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


package com.sun.cb.ws.client;

import com.sun.cb.ws.server.PriceListBean;
import com.sun.cb.ws.server.Supplier;
import com.sun.cb.ws.server.SupplierService;
import javax.xml.ws.WebServiceRef;


public final class PriceFetcher {
    //    @WebServiceRef(wsdlLocation="http://localhost:8080/jaxws-coffee-supplier/SupplierService?WSDL")
    //    static SupplierService service;
    public static final PriceListBean getPriceList(String endpoint) {
        PriceListBean result = null;

        try {
            //            Supplier port = service.getSupplierPort();
            Supplier port = new SupplierService()
                .getSupplierPort();
            result = (PriceListBean) port.getPriceList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    } // getPriceList    
} // class
