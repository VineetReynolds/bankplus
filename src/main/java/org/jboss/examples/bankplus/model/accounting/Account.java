package org.jboss.examples.bankplus.model.accounting;

import org.jboss.examples.bankplus.model.money.Money;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    @Column(name = "version")
    private int version;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(final AccountType accountType) {
        this.accountType = accountType;
    }

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name="amount",column=@Column(name="currentBalanceAmount")))
    @AssociationOverrides(
            @AssociationOverride(name="currency",joinColumns = @JoinColumn(name="currentBalanceCurrency")))
    private Money currentBalance;

    public Money getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(final Money balance) {
        this.currentBalance = balance;
    }

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name="amount",column=@Column(name="openingBalanceAmount")))
    @AssociationOverrides(
            @AssociationOverride(name="currency",joinColumns = @JoinColumn(name="openingBalanceCurrency")))
    private Money openingBalance;

    public Money getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Money openingBalance) {
        this.openingBalance = openingBalance;
    }

    @Temporal(TemporalType.DATE)
    private Date periodOpenDate;

    public Date getPeriodOpenDate() {
        return periodOpenDate;
    }

    public void setPeriodOpenDate(Date periodOpenDate) {
        this.periodOpenDate = periodOpenDate;
    }

    @ManyToOne
    private Account parentAccount;

    public Account getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(final Account parentAccount) {
        this.parentAccount = parentAccount;
        // Reset account type to that of parent account
        if(parentAccount != null) {
            this.accountType = parentAccount.getAccountType();
        }
    }

    @OneToMany
    private Set<Account> childAccounts = new HashSet<>();

    public Set<Account> getChildAccounts() {
        return childAccounts;
    }

    public void setChildAccounts(final Set<Account> childAccounts) {
        this.childAccounts = childAccounts;
    }

    @OneToMany
    private Set<JournalEntry> journalEntries = new HashSet<>();

    public Set<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(final Set<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
