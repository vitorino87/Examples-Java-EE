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

import com.sun.tutorial.javaee.dukesbank.entity.Account;
import com.sun.tutorial.javaee.dukesbank.entity.Customer;
import com.sun.tutorial.javaee.dukesbank.exception.AccountNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.CustomerNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.IllegalAccountTypeException;
import com.sun.tutorial.javaee.dukesbank.exception.InvalidParameterException;
import com.sun.tutorial.javaee.dukesbank.util.AccountDetails;
import com.sun.tutorial.javaee.dukesbank.util.Debug;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@DeclareRoles("bankAdmin")
@Stateful
public class AccountControllerBean implements AccountController {
    @PersistenceContext
    private EntityManager em;
    private String accountId;

    // account creation and removal methods
    @RolesAllowed("bankAdmin")
    public Long createAccount(
        AccountDetails details,
        Long customerId)
        throws IllegalAccountTypeException, CustomerNotFoundException,
            InvalidParameterException {
        // makes a new account and enters it into db,
        Account account = null;
        Customer customer = null;

        Debug.print("AccountControllerBean createAccount");

        if (details.getType() == null) {
            throw new InvalidParameterException("null type");
        } else if (details.getDescription() == null) {
            throw new InvalidParameterException("null description");
        } else if (details.getBeginBalanceTimeStamp() == null) {
            throw new InvalidParameterException("null beginBalanceTimeStamp");
        } else if (customerId == null) {
            throw new InvalidParameterException("null customerId");
        }

        try {
            customer = em.find(
                    Customer.class,
                    new Long(customerId));

            if (customer == null) {
                throw new CustomerNotFoundException();
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        try {
            account = new Account(
                        details.getType(),
                        details.getDescription(),
                        details.getBalance(),
                        details.getCreditLine(),
                        details.getBeginBalance(),
                        details.getBeginBalanceTimeStamp());
            em.persist(account);
            account.addCustomer(customer);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        return account.getId();
    }

    @RolesAllowed("bankAdmin")
    public void removeAccount(Long accountId)
        throws InvalidParameterException, AccountNotFoundException {
        // removes account
        Account account = null;

        Debug.print("AccountControllerBean removeAccount");

        if (accountId == null) {
            throw new InvalidParameterException("null accountId");
        }

        try {
            account = em.find(Account.class, accountId);

            if (account == null) {
                throw new AccountNotFoundException();
            } else {
                em.remove(account);
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    // customer-account relationship methods
    @RolesAllowed("bankAdmin")
    public void addCustomerToAccount(
        Long customerId,
        Long accountId)
        throws InvalidParameterException, CustomerNotFoundException,
            AccountNotFoundException {
        // adds another customer to the account
        Customer customer = null;
        Account account = null;

        Debug.print("AccountControllerBean addCustomerToAccount");

        if (customerId == null) {
            throw new InvalidParameterException("null customerId");
        } else if (accountId == null) {
            throw new InvalidParameterException("null accountId");
        }

        try {
            Debug.print(
                    "AccountControllerBean Getting the account: " + accountId);
            account = em.find(Account.class, accountId);

            if (account == null) {
                throw new AccountNotFoundException();
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        try {
            Debug.print(
                    "AccountControllerBean Getting the customer: " + customerId);
            customer = em.find(Customer.class, customerId);

            if (customer == null) {
                throw new CustomerNotFoundException();
            } else {
                Debug.print(
                        "AccountControllerBean: Adding the customer to the account.");
                account.addCustomer(customer);
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @RolesAllowed("bankAdmin")
    public void removeCustomerFromAccount(
        Long customerId,
        Long accountId)
        throws InvalidParameterException, CustomerNotFoundException,
            AccountNotFoundException {
        // removes a customer from this account, but
        // the customer is not removed from the db
        Account account = null;
        Customer customer = null;

        if (customerId == null) {
            throw new InvalidParameterException("null customerId");
        } else if (accountId == null) {
            throw new InvalidParameterException("null accountId");
        }

        Debug.print("AccountControllerBean removeCustomerFromAccount");

        try {
            account = em.find(Account.class, accountId);

            if (account == null) {
                throw new AccountNotFoundException();
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        try {
            customer = em.find(Customer.class, customerId);

            if (customer == null) {
                throw new CustomerNotFoundException();
            } else {
                account.removeCustomer(customer);
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public List<AccountDetails> getAccountsOfCustomer(Long customerId)
        throws InvalidParameterException, CustomerNotFoundException {
        // returns a List of AccountDetails
        // that correspond to the accounts for the specified
        // customer
        Debug.print("AccountControllerBean getAccountsOfCustomer");

        Collection accounts = null;
        Customer customer = null;

        if (customerId == null) {
            throw new InvalidParameterException("null customerId");
        }

        try {
            customer = em.find(Customer.class, customerId);

            if (customer == null) {
                throw new CustomerNotFoundException();
            } else {
                accounts = customer.getAccounts();
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        return copyAccountsToDetails(accounts);
    }

    public List<Long> getCustomerIds(Long accountId)
        throws InvalidParameterException, AccountNotFoundException {
        Debug.print("AccountControllerBean getCustomerIds");

        List<Customer> customers = null;

        if (accountId == null) {
            throw new InvalidParameterException("null accountId");
        }

        try {
            customers = em.createNamedQuery(
                        "Customer.FindAllCustomersOfAccount")
                          .setParameter("accountId", accountId)
                          .getResultList();

            if (customers == null) {
                throw new AccountNotFoundException();
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        return copyCustomerIdsToList(customers);
    }

    public AccountDetails getDetails(Long accountId)
        throws InvalidParameterException, AccountNotFoundException {
        Debug.print("AccountControllerBean getDetails");

        AccountDetails details = null;
        Account account = null;

        if (accountId == null) {
            throw new InvalidParameterException("null accountId");
        }

        try {
            account = em.find(Account.class, accountId);

            if (account == null) {
                System.err.println(
                        "AccountControllerBean.getDetails: Account not found.");
                throw new AccountNotFoundException(accountId);
            } else {
                details = new AccountDetails(
                            accountId,
                            account.getType(),
                            account.getDescription(),
                            account.getBalance(),
                            account.getCreditLine(),
                            account.getBeginBalance(),
                            account.getBeginBalanceTimeStamp());

                List<Long> customerIds = this.getCustomerIds(accountId);
                details.setCustomerIds(customerIds);
            }
        } catch (Exception ex) {
            System.err.println(
                    "AccountControllerBean.getDetails: Caught an exception.");
            ex.getStackTrace();
        }

        return details;
    }

    private List<AccountDetails> copyAccountsToDetails(Collection accounts) {
        List<AccountDetails> detailsList = new ArrayList<AccountDetails>();
        Iterator i = accounts.iterator();

        while (i.hasNext()) {
            Account account = (Account) i.next();
            AccountDetails details = new AccountDetails(
                        account.getId(),
                        account.getType(),
                        account.getDescription(),
                        account.getBalance(),
                        account.getCreditLine(),
                        account.getBeginBalance(),
                        account.getBeginBalanceTimeStamp());
            detailsList.add(details);
        }

        return detailsList;
    }

    private List<Long> copyCustomerIdsToList(List<Customer> customers) {
        List<Long> customerIdList = new ArrayList<Long>();
        Iterator<Customer> i = customers.iterator();

        while (i.hasNext()) {
            Customer customer = (Customer) i.next();
            customerIdList.add(customer.getCustomerId());
        }

        return customerIdList;
    }
}
