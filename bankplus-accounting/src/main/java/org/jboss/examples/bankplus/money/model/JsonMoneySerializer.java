package org.jboss.examples.bankplus.money.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class JsonMoneySerializer extends JsonSerializer<Money> {
    @Override
    public void serialize(Money money, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("ccy", money.getCurrency().getCurrencyCode());
        jsonGenerator.writeNumberField("amount", money.getAmount());
        jsonGenerator.writeEndObject();
    }
}
