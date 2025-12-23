package ma.emsi.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entité Commande représentant la table 'commande' en base.
 * Relation Many-to-One avec Client.
 */
public class Commande {
    
    private Long id;
    private LocalDate dateCommande;
    private BigDecimal montant;
    private Client client; // Référence vers le client propriétaire

    // Constructeurs
    public Commande() {
    }

    public Commande(LocalDate dateCommande, BigDecimal montant) {
        this.dateCommande = dateCommande;
        this.montant = montant;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", dateCommande=" + dateCommande +
                ", montant=" + montant +
                ", clientNom=" + (client != null ? client.getNom() : "null") +
                '}';
    }
}