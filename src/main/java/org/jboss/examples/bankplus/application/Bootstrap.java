package org.jboss.examples.bankplus.application;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jboss.examples.bankplus.accounting.model.Account;
import org.jboss.examples.bankplus.accounting.model.AccountType;
import org.jboss.examples.bankplus.accounting.services.Accounts;
import org.jboss.examples.bankplus.money.model.Currency;
import org.jboss.examples.bankplus.money.model.Money;
import org.jboss.examples.bankplus.money.services.Currencies;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.function.Consumer;

@Stateless
public class Bootstrap {

    private final ImmutableSet<ImmutableMap<String, String>> ACCOUNTS = ImmutableSet.<ImmutableMap<String, String>>builder()
            .add(ImmutableMap.of("id", "1000", "name", "Assets", "type", "ASSET", "parent", ""))
            .add(ImmutableMap.of("id", "2000", "name", "Liabilities", "type", "LIABILITY", "parent", ""))
            .add(ImmutableMap.of("id", "3000", "name", "Equity", "type", "EQUITY", "parent", ""))
            .add(ImmutableMap.of("id", "4000", "name", "Income", "type", "REVENUE", "parent", ""))
            .add(ImmutableMap.of("id", "5000", "name", "Expenses", "type", "EXPENSE", "parent", ""))
            .add(ImmutableMap.of("id", "1001", "name", "Cash", "type", "ASSET", "parent", "1000"))
            .add(ImmutableMap.of("id", "4100", "name", "Charges", "type", "REVENUE", "parent", "4000"))
            .add(ImmutableMap.of("id", "2001", "name", "Clearing", "type", "LIABILITY", "parent", "2000"))
            .build();

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Accounts accounts;

    @Inject
    private Currencies currencies;

    public void createAccounts() {
        Currency USD = currencies.create("United States Dollar", "USD");
        ACCOUNTS.forEach(accountInfo -> {
            String accountId = accountInfo.get("id");
            String name = accountInfo.get("name");
            AccountType accountType = Enum.valueOf(AccountType.class, accountInfo.get("type"));
            String parent = accountInfo.get("parent");
            Account parentAccount = null;
            if(!parent.isEmpty()) {
                parentAccount = accounts.findByAccountId(parent);
            }
            accounts.newAccount(accountId, name, accountType, parentAccount, new Money(USD, BigDecimal.ZERO));
            em.flush();
        });
    }
}
