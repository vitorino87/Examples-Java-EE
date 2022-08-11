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


package com.sun.tutorial.javaee.dukesbank.request;

import com.sun.tutorial.javaee.dukesbank.exception.AccountNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.CustomerNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.IllegalAccountTypeException;
import com.sun.tutorial.javaee.dukesbank.exception.InvalidParameterException;
import com.sun.tutorial.javaee.dukesbank.util.AccountDetails;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;


@Remote
public interface AccountController {
    @RolesAllowed("bankAdmin")
    void addCustomerToAccount(
        Long customerId,
        Long accountId)
        throws InvalidParameterException, CustomerNotFoundException,
            AccountNotFoundException;

    @RolesAllowed("bankAdmin")
    Long createAccount(
        AccountDetails details,
        Long customerId)
        throws IllegalAccountTypeException, CustomerNotFoundException,
            InvalidParameterException;

    List<AccountDetails> getAccountsOfCustomer(Long customerId)
        throws InvalidParameterException, CustomerNotFoundException;

    List<Long> getCustomerIds(Long accountId)
        throws InvalidParameterException, AccountNotFoundException;

    AccountDetails getDetails(Long accountId)
        throws InvalidParameterException, AccountNotFoundException;

    @RolesAllowed("bankAdmin")
    void removeAccount(Long accountId)
        throws InvalidParameterException, AccountNotFoundException;

    @RolesAllowed("bankAdmin")
    void removeCustomerFromAccount(
        Long customerId,
        Long accountId)
        throws InvalidParameterException, CustomerNotFoundException,
            AccountNotFoundException;
}
