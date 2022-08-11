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

import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import com.sun.tutorial.javaee.dukesbank.exception.AccountNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.InsufficientCreditException;
import com.sun.tutorial.javaee.dukesbank.exception.InsufficientFundsException;
import com.sun.tutorial.javaee.dukesbank.exception.InvalidParameterException;
import com.sun.tutorial.javaee.dukesbank.util.AccountDetails;
import com.sun.tutorial.javaee.dukesbank.util.Debug;
import com.sun.tutorial.javaee.dukesbank.web.Util.Navigation;


public class TransferFundsBean {
    private AccountDetails fromAccount;
    private AccountDetails toAccount;
    private BigDecimal transferAmount;
    private CustomerBean customer;
    private Long customerId;
    private Long fromAccountId;
    private Long toAccountId;

    public boolean doTx() {
        try {
            customer.getTxController()
                    .transferFunds(
                    transferAmount,
                    "Transfer",
                    fromAccountId,
                    toAccountId);
        } catch (InvalidParameterException e) {
            // Not possible
        } catch (AccountNotFoundException e) {
            // Not possible
        } catch (InsufficientFundsException e) {
            Util.addErrorMessage(
                    FacesContext.getCurrentInstance(),
                    null,
                    "InsufficientFundsError");

            return false;
        } catch (InsufficientCreditException e) {
            Util.addErrorMessage(
                    FacesContext.getCurrentInstance(),
                    null,
                    "InsufficientCreditError");

            return false;
        }

        return true;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public AccountDetails getFromAccount() {
        if (fromAccountId != null) {
            customer.setActiveAccount(fromAccountId);
            fromAccount = customer.getAccountDetails();
        }

        return fromAccount;
    }

    public AccountDetails getToAccount() {
        if (toAccountId != null) {
            customer.setActiveAccount(toAccountId);
            toAccount = customer.getAccountDetails();
        }

        return toAccount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
        Debug.print("TFB: Setting transfer amount to: " + transferAmount);
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
        Debug.print("TFB: Setting from account id to: " + fromAccountId);
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
        Debug.print("TFB: Setting to account id to: " + toAccountId);
    }

    public void setFromAccount(AccountDetails fromAccount) {
        this.fromAccount = fromAccount;
        Debug.print("TFB: Setting from account to: " + fromAccount);
    }

    public void setToAccount(AccountDetails toAccount) {
        this.toAccount = toAccount;
        Debug.print("TFB: Setting to account to: " + toAccount);
    }

    public Object submit() {
        if (fromAccountId.equals(toAccountId)) {
            Util.addErrorMessage(
                    FacesContext.getCurrentInstance(),
                    null,
                    "SameAccounts");

            return Navigation.transferFunds;
        }

        if (doTx()) {
            return Navigation.transferAck;
        } else {
            return null;
        }
    }
}
