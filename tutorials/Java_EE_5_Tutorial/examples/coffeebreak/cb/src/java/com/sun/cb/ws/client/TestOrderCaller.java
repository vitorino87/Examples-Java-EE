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

import com.sun.cb.ws.server.AddressBean;
import com.sun.cb.ws.server.ConfirmationBean;
import com.sun.cb.ws.server.CustomerBean;
import com.sun.cb.common.DateHelper;
import com.sun.cb.ws.server.LineItemBean;
import com.sun.cb.ws.server.OrderBean;
import com.sun.cb.common.URLHelper;
import java.math.BigDecimal;


public class TestOrderCaller {
    public static void main(String[] args) {
        try {
            AddressBean address = new AddressBean();
            address.setStreet("455 Apple Way");
            address.setCity("Santa Clara");
            address.setState("CA");
            address.setZip("95123");

            CustomerBean customer = new CustomerBean();
            customer.setFirstName("Buzz");
            customer.setLastName("Murphy");
            customer.setPhoneNumber("247-5566");
            customer.setEmailAddress("buzz.murphy@clover.com");

            LineItemBean itemA = new LineItemBean();
            itemA.setCoffeeName("mocha");
            itemA.setPounds(new BigDecimal("1.0"));
            itemA.setPrice(new BigDecimal("9.50"));

            LineItemBean itemB = new LineItemBean();
            itemB.setCoffeeName("special blend");
            itemB.setPounds(new BigDecimal("5.0"));
            itemB.setPrice(new BigDecimal("8.00"));

            LineItemBean itemC = new LineItemBean();
            itemC.setCoffeeName("wakeup call");
            itemC.setPounds(new BigDecimal("0.5"));
            itemC.setPrice(new BigDecimal("10.00"));

            LineItemBean[] lineItems = { itemA, itemB, itemC };

            OrderBean order = new OrderBean();
            order.setAddress(address);
            order.setCustomer(customer);
            order.setId("123");
            order.getLineItems()
                 .add(itemA);
            order.getLineItems()
                 .add(itemB);
            order.getLineItems()
                 .add(itemC);
            order.setTotal(new BigDecimal("55.67"));

            OrderCaller oc = new OrderCaller(URLHelper.getEndpointURL());
            ConfirmationBean confirmation = oc.placeOrder(order);

            System.out.println(
                    confirmation.getOrderId() + " "
                    + DateHelper.format(
                            confirmation.getShippingDate().toGregorianCalendar(),
                            "MM/dd/yy"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
