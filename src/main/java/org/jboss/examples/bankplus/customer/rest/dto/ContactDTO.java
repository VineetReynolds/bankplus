package org.jboss.examples.bankplus.customer.rest.dto;

import java.io.Serializable;

import org.jboss.examples.bankplus.customer.model.Contact;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactDTO implements Serializable {

    private Long id;
    private String fullName;
    private String iban;

    public ContactDTO() {
    }

    public ContactDTO(final Contact entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fullName = entity.getFullName();
            this.iban = entity.getIban();
        }
    }

    public Contact fromDTO(Contact entity, EntityManager em) {
        if (entity == null) {
            entity = new Contact();
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