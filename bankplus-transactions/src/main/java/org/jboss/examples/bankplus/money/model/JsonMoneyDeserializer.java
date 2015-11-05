package org.jboss.examples.bankplus.money.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.examples.bankplus.money.services.Currencies;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.math.BigDecimal;

public class JsonMoneyDeserializer extends JsonDeserializer<Money> {
    @Override
    public Money deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String currencyCode = node.get("ccy").textValue();
        BigDecimal amount = node.get("amount").decimalValue();
        BeanManager bm = getBeanManager();
        Bean<Currencies> bean = (Bean<Currencies>) bm.getBeans(Currencies.class).iterator().next();
        CreationalContext<Currencies> ctx = bm.createCreationalContext(bean);
        Currencies currencies = (Currencies) bm.getReference(bean, Currencies.class, ctx);
        return new Money(currencies.findByCode(currencyCode), amount);
    }

    public BeanManager getBeanManager()
    {
        try {
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/BeanManager");
        }    catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
}
