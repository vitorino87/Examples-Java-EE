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


package com.sun.tutorial.javaee.dukesbank.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJBException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "BANK_ACCOUNT")
@NamedQueries({
    @NamedQuery(name = "Account.FindById",query = "SELECT a FROM Account a WHERE a.id = :id")
    , @NamedQuery(name = "Account.FindByType", query = "SELECT a FROM Account a WHERE a.type = :type")
    , @NamedQuery(name = "Account.FindByDescription", query = "SELECT a FROM Account a WHERE a.description = :description")
    , @NamedQuery(name = "Account.FindByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance")
    , @NamedQuery(name = "Account.FindByCreditLine", query = "SELECT a FROM Account a WHERE a.creditLine = :creditLine")
    , @NamedQuery(name = "Account.FindByBeginBalance", query = "SELECT a FROM Account a WHERE a.beginBalance = :beginBalance")
    , @NamedQuery(name = "Account.FindByBeginBalanceTimeStamp", query = "SELECT a FROM Account a WHERE a.beginBalanceTimeStamp = :beginBalanceTimeStamp")
})
public class Account implements java.io.Serializable {
    @Column(name = "BALANCE")
    private BigDecimal balance;
    @Column(name = "BEGIN_BALANCE")
    private BigDecimal beginBalance;
    @Column(name = "CREDIT_LINE")
    private BigDecimal creditLine;
    @JoinTable(name = "BANK_CUSTOMER_ACCOUNT_XREF", joinColumns = @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
    , inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    )
    @ManyToMany
    private Collection<Customer> customers;
    @Column(name = "BEGIN_BALANCE_TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginBalanceTimeStamp;
    @TableGenerator(name = "accountIdGen", table = "BANK_SEQUENCE_GENERATOR", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "ACCOUNT_ID", initialValue = 5050, allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "accountIdGen")
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long id;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TYPE")
    private String type;

    /** Creates a new instance of Account */
    public Account() {
    }

    public Account(
        String type,
        String description,
        BigDecimal balance,
        BigDecimal creditLine,
        BigDecimal beginBalance,
        Date beginBalanceTimeStamp) {
        this.type = type;
        this.description = description;
        this.balance = balance;
        this.creditLine = creditLine;
        this.beginBalance = beginBalance;
        this.beginBalanceTimeStamp = beginBalanceTimeStamp;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long accountId) {
        this.id = accountId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLine() {
        return this.creditLine;
    }

    public void setCreditLine(BigDecimal creditLine) {
        this.creditLine = creditLine;
    }

    public BigDecimal getBeginBalance() {
        return this.beginBalance;
    }

    public void setBeginBalance(BigDecimal beginBalance) {
        this.beginBalance = beginBalance;
    }

    public Date getBeginBalanceTimeStamp() {
        return this.beginBalanceTimeStamp;
    }

    public void setBeginBalanceTimeStamp(Date beginBalanceTimeStamp) {
        this.beginBalanceTimeStamp = beginBalanceTimeStamp;
    }

    public Collection<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Collection<Customer> customers) {
        this.customers = customers;
    }

    public void addCustomer(Customer customer) {
        try {
            this.customers.add(customer);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public void removeCustomer(Customer customer) {
        try {
            this.customers.remove(customer);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
}
