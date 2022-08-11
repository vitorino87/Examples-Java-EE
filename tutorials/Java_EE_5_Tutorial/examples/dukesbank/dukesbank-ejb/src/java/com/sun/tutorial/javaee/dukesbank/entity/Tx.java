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
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "BANK_TX")
@NamedQueries({
    @NamedQuery(name = "Tx.FindById",query = "SELECT a FROM Tx a WHERE a.id = :id")
    , @NamedQuery(name = "Tx.FindByTimeStamp", query = "SELECT a FROM Tx a WHERE a.timeStamp = :timeStamp")
    , @NamedQuery(name = "Tx.FindByAmount", query = "SELECT a FROM Tx a WHERE a.amount = :amount")
    , @NamedQuery(name = "Tx.FindByBalance", query = "SELECT a FROM Tx a WHERE a.balance = :balance")
    , @NamedQuery(name = "Tx.FindByDescription", query = "SELECT a FROM Tx a WHERE a.description = :description")
    , @NamedQuery(name = "Tx.FindTxsByDates", query = "SELECT t FROM Tx t JOIN t.account a WHERE a.id = :accountId "
    + "AND t.timeStamp BETWEEN :startDate AND :endDate")
})
public class Tx implements java.io.Serializable {
    @JoinColumn(name = "ACCOUNT_ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "BALANCE")
    private BigDecimal balance;
    @Column(name = "TIME_STAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;
    @TableGenerator(name = "txIdGen", table = "BANK_SEQUENCE_GENERATOR", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "TX_ID", initialValue = 100, allocationSize = 10)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "txIdGen")
    @Column(name = "TX_ID", nullable = false)
    private Long id;
    @Column(name = "DESCRIPTION")
    private String description;

    /** Creates a new instance of Tx */
    public Tx() {
    }

    public Tx(
        Account account,
        Date timeStamp,
        BigDecimal amount,
        BigDecimal balance,
        String description) {
        this.account = account;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.balance = balance;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long txId) {
        this.id = txId;
    }

    public Date getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account accountId) {
        this.account = accountId;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object object) {
        return object instanceof Tx && id.equals(((Tx) object).getId());
    }

    public String toString() {
        //TODO change toString() implementation to return a better display name
        return "" + this.id;
    }
}
