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


package order.request;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Remote;


@Remote
public interface Request {
    void addLineItem(
        Integer orderId,
        String partNumber,
        int revision,
        int quantity);

    void addPartToBillOfMaterial(
        String bomPartNumber,
        int bomRevision,
        String partNumber,
        int revision);

    void adjustOrderDiscount(int adjustment);

    int countAllItems();

    void createOrder(
        Integer orderId,
        char status,
        int discount,
        String shipmentInfo);

    void createPart(
        String partNumber,
        int revision,
        String description,
        Date revisionDate,
        String specification,
        Serializable drawing);

    void createVendor(
        int vendorId,
        String name,
        String address,
        String contact,
        String phone);

    void createVendorPart(
        String partNumber,
        int revision,
        String description,
        double price,
        int vendorId);

    Double getAvgPrice();

    double getBillOfMaterialPrice(
        String bomPartNumber,
        int bomRevision,
        String partNumber,
        int revision);

    double getOrderPrice(Integer orderId);

    Double getTotalPricePerVendor(int vendorId);

    Collection<String> locateVendorsByPartialName(String name);

    void removeOrder(Integer orderId);

    String reportVendorsByOrder(Integer orderId);
}
