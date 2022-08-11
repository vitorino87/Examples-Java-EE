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
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.sun.tutorial.javaee.dukesbank.exception.AccountNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.IllegalAccountTypeException;
import com.sun.tutorial.javaee.dukesbank.exception.InsufficientCreditException;
import com.sun.tutorial.javaee.dukesbank.exception.InsufficientFundsException;
import com.sun.tutorial.javaee.dukesbank.exception.InvalidParameterException;
import com.sun.tutorial.javaee.dukesbank.util.AccountDetails;
import com.sun.tutorial.javaee.dukesbank.util.Debug;
import com.sun.tutorial.javaee.dukesbank.web.Util.Navigation;


public class ATMBean {
    private AccountDetails selectedAccount;
    private BigDecimal amount;
    private CustomerBean customer;
    private String operationString;
    private String prepString;
    private int operation;

    public boolean doTx() {
        FacesContext context = FacesContext.getCurrentInstance();
        operationString = Util.getString(context, "WithdrewString");
        prepString = Util.getString(context, "FromString");
        selectedAccount = customer.getAccountDetails();

        boolean isCreditAcct = false;

        if (selectedAccount.getType()
                               .equals("Credit")) {
            isCreditAcct = true;
        }

        if (isCreditAcct) {
            if (operation == 0) {
                try {
                    customer.getTxController()
                            .makeCharge(
                            amount,
                            "ATM Withdrawal",
                            customer.getActiveAccount());
                } catch (InvalidParameterException e) {
                    // Not possible
                } catch (AccountNotFoundException e) {
                    // Not possible
                } catch (InsufficientCreditException e) {
                    Util.addErrorMessage(
                            context,
                            null,
                            "InsufficientCreditError");

                    return false;
                } catch (IllegalAccountTypeException e) {
                    // Not possible
                }
            } else {
                operationString = Util.getString(context, "DepositedString");
                prepString = Util.getString(context, "ToString");

                try {
                    customer.getTxController()
                            .makePayment(
                            amount,
                            "ATM Deposit",
                            customer.getActiveAccount());
                } catch (InvalidParameterException e) {
                    // Not possible
                } catch (AccountNotFoundException e) {
                    // Not possible
                } catch (IllegalAccountTypeException e) {
                    // Not possible
                }
            }
        } else {
            if (operation == 0) {
                try {
                    customer.getTxController()
                            .withdraw(
                            amount,
                            "ATM Withdrawal",
                            customer.getActiveAccount());
                } catch (InvalidParameterException e) {
                    // Not possible
                } catch (AccountNotFoundException e) {
                    // Not possible
                } catch (IllegalAccountTypeException e) {
                    // Not possible
                } catch (InsufficientFundsException e) {
                    Util.addErrorMessage(
                            context,
                            null,
                            "InsufficientFundsError");

                    return false;
                }
            } else {
                operationString = Util.getString(context, "DepositedString");
                prepString = Util.getString(context, "ToString");

                try {
                    customer.getTxController()
                            .deposit(
                            amount,
                            "ATM Deposit",
                            customer.getActiveAccount());
                } catch (InvalidParameterException e) {
                    // Not possible
                } catch (AccountNotFoundException e) {
                    // Not possible
                } catch (IllegalAccountTypeException e) {
                    // Not possible
                }
            }
        }

        selectedAccount = customer.getAccountDetails();

        return true;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getOperationString() {
        return operationString;
    }

    public String getPrepString() {
        return prepString;
    }

    public AccountDetails getSelectedAccount() {
        return selectedAccount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        Debug.print("Setting amount to: " + amount);
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
        Debug.print("Setting operation to: " + operation);
    }

    public List<SelectItem> getOperationOptions() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        String withdraw = Util.getString(context, "Withdraw");
        String deposit = Util.getString(context, "Deposit");

        List<SelectItem> options = new ArrayList<SelectItem>(2);
        options.add(new SelectItem(0, withdraw));
        options.add(new SelectItem(1, deposit));

        return options;
    }

    public Long getAccountId() {
        return customer.getActiveAccount();
    }

    public void setAccountId(Long accountId) {
        customer.setActiveAccount(accountId);
        Debug.print("Setting account id to: " + accountId);
    }

    public void setOperationString(String operation) {
        operationString = operation;
    }

    public void setPrepString(String prep) {
        prepString = prep;
    }

    public void setSelectedAccount(AccountDetails account) {
        selectedAccount = account;
    }

    public Object submit() {
        if (doTx()) {
            return Navigation.atmAck;
        } else {
            return Navigation.atm;
        }
    }
}
