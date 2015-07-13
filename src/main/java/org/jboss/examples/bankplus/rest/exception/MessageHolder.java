package org.jboss.examples.bankplus.rest.exception;

import org.jboss.examples.bankplus.services.BusinessException;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class MessageHolder implements Serializable {

    private String message;

    public MessageHolder(BusinessException ex) {
        this.message = ex.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
