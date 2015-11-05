package org.jboss.examples.bankplus.customer.rest.representations;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Contact implements Serializable {

    private Long id;
    private String fullName;
    private String iban;

    public Contact() {
    }

    public Contact(final org.jboss.examples.bankplus.customer.model.Contact entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fullName = entity.getFullName();
            this.iban = entity.getIban();
        }
    }

    public org.jboss.examples.bankplus.customer.model.Contact fromDTO(org.jboss.examples.bankplus.customer.model.Contact entity, EntityManager em) {
        if (entity == null) {
            entity = new org.jboss.examples.bankplus.customer.model.Contact();
        }
        entity.setFullName(this.fullName);
        entity.setIban(this.iban);
        entity = em.merge(entity);
        return entity;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public String getIban() {
        return this.iban;
    }

    public void setIban(final String iban) {
        this.iban = iban;
    }
}