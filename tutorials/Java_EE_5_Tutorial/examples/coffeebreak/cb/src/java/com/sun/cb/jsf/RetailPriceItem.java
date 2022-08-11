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


package com.sun.cb.jsf;

import java.io.Serializable;
import java.math.BigDecimal;


public class RetailPriceItem implements Serializable {
    private BigDecimal retailPricePerPound;
    private BigDecimal wholesalePricePerPound;
    private String coffeeName;
    private String supplier;

    public RetailPriceItem() {
        this.coffeeName = null;
        this.wholesalePricePerPound = new BigDecimal("0.00");
        this.retailPricePerPound = new BigDecimal("0.00");
        this.supplier = null;
    }

    public RetailPriceItem(
        String coffeeName,
        BigDecimal wholesalePricePerPound,
        BigDecimal retailPricePerPound,
        String supplier) {
        this.coffeeName = coffeeName;
        this.wholesalePricePerPound = wholesalePricePerPound;
        this.retailPricePerPound = retailPricePerPound;
        this.supplier = supplier;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public BigDecimal getWholesalePricePerPound() {
        return wholesalePricePerPound;
    }

    public BigDecimal getRetailPricePerPound() {
        return retailPricePerPound;
    }

    public void setRetailPricePerPound(BigDecimal retailPricePerPound) {
        this.retailPricePerPound = retailPricePerPound;
    }

    public void setWholesalePricePerPound(BigDecimal wholesalePricePerPound) {
        this.wholesalePricePerPound = wholesalePricePerPound;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
