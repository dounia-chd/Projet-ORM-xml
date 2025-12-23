package ma.emsi.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité Client représentant la table 'client' en base.
 * Utilise le mapping XML (Client.hbm.xml).
 */
public class Client {
    
    // Attributs
    private Long id;
    private String nom;
    private String email;
    private Set<Commande> commandes = new HashSet<>(); // Collection de commandes

    // Constructeurs
    public Client() {
        // Constructeur vide requis par Hibernate
    }

    public Client(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    // Méthodes utilitaires
    public void addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setClient(this);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", nbCommandes=" + commandes.size() +
                '}';
    }
}