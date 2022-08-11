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


package com.sun.tutorial.javaee.dukesbank.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * This class holds the details of a bank account entity.
 * It contains getters and setters for each variable.
 */
public class AccountDetails implements java.io.Serializable {
    private BigDecimal balance;
    private BigDecimal beginBalance;
    private BigDecimal creditLine;
    private Date beginBalanceTimeStamp;
    private List<Long> customerIds;
    private Long accountId;
    private String description;
    private String type;

    public AccountDetails(
        Long accountId,
        String type,
        String description,
        BigDecimal balance,
        BigDecimal creditLine,
        BigDecimal beginBalance,
        Date beginBalanceTimeStamp) {
        this.accountId = accountId;
        this.type = type;
        this.description = description;
        this.balance = balance;
        this.creditLine = creditLine;
        this.beginBalance = beginBalance;
        this.beginBalanceTimeStamp = beginBalanceTimeStamp;
    }

    public AccountDetails(
        String type,
        String description,
        BigDecimal balance,
        BigDecimal creditLine,
        BigDecimal beginBalance,
        Date beginBalanceTimeStamp) {
        this.accountId = accountId;
        this.type = type;
        this.description = description;
        this.balance = balance;
        this.creditLine = creditLine;
        this.beginBalance = beginBalance;
        this.beginBalanceTimeStamp = beginBalanceTimeStamp;
    }

    public AccountDetails(
        Long accountId,
        String type,
        String description,
        List<Long> customerIds,
        BigDecimal balance,
        BigDecimal creditLine,
        BigDecimal beginBalance,
        Date beginBalanceTimeStamp) {
        this.accountId = accountId;
        this.description = description;
        this.customerIds = customerIds;
        this.balance = balance;
        this.creditLine = creditLine;
        this.beginBalance = beginBalance;
        this.beginBalanceTimeStamp = beginBalanceTimeStamp;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getCreditLine() {
        return creditLine;
    }

    public BigDecimal getBeginBalance() {
        return beginBalance;
    }

    public Date getBeginBalanceTimeStamp() {
        return beginBalanceTimeStamp;
    }

    public BigDecimal getRemainingCredit() {
        return creditLine.subtract(balance);
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCreditLine(BigDecimal creditLine) {
        this.creditLine = creditLine;
    }

    public void setBeginBalance(BigDecimal beginBalance) {
        this.beginBalance = beginBalance;
    }

    public void setBeginBalanceTimeStamp(Date beginBalanceTimeStamp) {
        this.beginBalanceTimeStamp = beginBalanceTimeStamp;
    }
}
