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

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.List;
import com.sun.tutorial.javaee.dukesbank.exception.AccountNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.CustomerNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.InvalidParameterException;
import com.sun.tutorial.javaee.dukesbank.request.AccountController;
import com.sun.tutorial.javaee.dukesbank.request.TxController;
import com.sun.tutorial.javaee.dukesbank.util.AccountDetails;
import com.sun.tutorial.javaee.dukesbank.util.Debug;
import com.sun.tutorial.javaee.dukesbank.web.Util.Navigation;


public class CustomerBean {
    @EJB
    private AccountController accountController;
    private Long account;
    private Long customerId;
    @EJB
    private TxController txController;

    public CustomerBean() {
        customerId = Long.parseLong(
                    FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
    }

    public TxController getTxController() {
        return txController;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setActiveAccount(Long account) {
        this.account = account;
    }

    public Long getActiveAccount() {
        return this.account;
    }

    public AccountDetails getAccountDetails() {
        AccountDetails ad = null;

        try {
            ad = accountController.getDetails(this.account);
        } catch (InvalidParameterException e) {
            Debug.print(e.getMessage());

            // Not possible
        } catch (AccountNotFoundException e) {
            Debug.print(e.getMessage());

            // Not possible
        }

        if (ad != null) {
            Debug.print(
                "account ID: ",
                ad.getAccountId());
        }

        return ad;
    }

    public List<AccountDetails> getAccounts() {
        List<AccountDetails> accounts = null;

        try {
            accounts = accountController.getAccountsOfCustomer(customerId);
        } catch (InvalidParameterException e) {
            Debug.print(e.getMessage());

            // Not possible
        } catch (CustomerNotFoundException e) {
            Debug.print(e.getMessage());

            // Not possible
        }

        return accounts;
    }

    public Object logout() {
        HttpSession session = (HttpSession) Util.getExternalContext()
                                                .getSession(true);
        session.invalidate();

        return Navigation.main;
    }
}
