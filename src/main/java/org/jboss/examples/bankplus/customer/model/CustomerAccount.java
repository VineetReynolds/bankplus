package org.jboss.examples.bankplus.customer.model;

import javax.persistence.*;

@Entity
public class CustomerAccount {

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

    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @OneToOne(mappedBy = "customerAccount")
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Embedded
    private Account financialAccount;

    public Account getFinancialAccount() {
        return financialAccount;
    }

    public void setFinancialAccount(Account financialAccount) {
        this.financialAccount = financialAccount;
    }
}
