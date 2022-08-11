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


package order.entity;

import java.io.Serializable;


public class LineItemKey implements Serializable {
    private Integer orderId;
    private int itemId;

    public LineItemKey() {
    }

    public LineItemKey(
        Integer orderId,
        int itemId) {
        this.setOrderId(orderId);
        this.setItemId(itemId);
    }

    public int hashCode() {
        return (((this.getOrderId() == null) ? 0 : this.getOrderId()
                                                       .hashCode())
        ^ ((int) this.getItemId()));
    }

    public boolean equals(Object otherOb) {
        if (this == otherOb) {
            return true;
        }

        if (!(otherOb instanceof LineItemKey)) {
            return false;
        }

        LineItemKey other = (LineItemKey) otherOb;

        return (((this.getOrderId() == null) ? (other.getOrderId() == null)
                                             : this.getOrderId()
                                                   .equals(other.getOrderId()))
        && (this.getItemId() == other.getItemId()));
    }

    public String toString() {
        return "" + getOrderId() + "-" + getItemId();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
