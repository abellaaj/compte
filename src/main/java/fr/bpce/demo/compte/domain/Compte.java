package fr.bpce.demo.compte.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Compte.
 */
@Document(collection = "compte")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "compte")
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("libelle")
    private String libelle;

    @Field("banque")
    private String banque;

    @Field("iban")
    private String iban;

    @Field("devise")
    private String devise;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Compte libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getBanque() {
        return banque;
    }

    public Compte banque(String banque) {
        this.banque = banque;
        return this;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public String getIban() {
        return iban;
    }

    public Compte iban(String iban) {
        this.iban = iban;
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getDevise() {
        return devise;
    }

    public Compte devise(String devise) {
        this.devise = devise;
        return this;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Compte compte = (Compte) o;
        if (compte.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), compte.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Compte{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", banque='" + getBanque() + "'" +
            ", iban='" + getIban() + "'" +
            ", devise='" + getDevise() + "'" +
            "}";
    }
}
