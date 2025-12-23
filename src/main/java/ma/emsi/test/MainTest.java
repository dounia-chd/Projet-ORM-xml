package ma.emsi.test;

import ma.emsi.entities.Client;
import ma.emsi.entities.Commande;
import ma.emsi.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Classe de test pour démontrer les opérations CRUD avec Hibernate ORM.
 */
public class MainTest {
    
    public static void main(String[] args) {
        
        System.out.println("=== Démarrage des tests ORM ===\n");
        
        // Test 1 : Création (Create)
        testCreate();
        
        // Test 2 : Lecture (Read)
        testRead();
        
        // Test 3 : Lecture avec relation (Read with Join)
        testReadWithCommandes();
        
        // Test 3b : HQL avancé (montant > 1000)
        testHqlMontantSuperieur();

        // Test 3c : API Critères (email LIKE %emsi.ma)
        testCriteriaEmailLike();

        // Test 4 : Mise à jour (Update)
        testUpdate();
        
        // Test 5 : Suppression (Delete)
        testDelete();
        
        // Fermeture propre
        HibernateUtil.shutdown();
        System.out.println("\n=== Fin des tests ===");
    }
    
    /**
     * Test 1 : Création d'un client et ses commandes
     */
    private static void testCreate() {
        System.out.println("--- Test CREATE ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            // Création d'un nouveau client
            Client client = new Client("Imane El Amrani", "imane.elamrani@emsi.ma");
            
            // Création de 2 commandes
            Commande cmd1 = new Commande(LocalDate.now(), new BigDecimal("550.00"));
            Commande cmd2 = new Commande(LocalDate.now().plusDays(2), new BigDecimal("1200.00"));
            
            // Association bidirectionnelle
            client.addCommande(cmd1);
            client.addCommande(cmd2);
            
            // Sauvegarde (cascade propagera aux commandes)
            session.save(client);
            
            tx.commit();
            System.out.println("✓ Client créé avec ID: " + client.getId());
            
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("✗ Erreur lors de la création: " + e.getMessage());
        } finally {
            session.close();
        }
    }
    
    /**
     * Test 2 : Lecture d'un client par ID
     */
    private static void testRead() {
        System.out.println("\n--- Test READ ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            // Récupération du client ID=1
            Client client = session.get(Client.class, 1L);
            
            if (client != null) {
                System.out.println("✓ Client trouvé: " + client);
            } else {
                System.out.println("✗ Aucun client avec ID=1");
            }
            
        } finally {
            session.close();
        }
    }
    
    /**
     * Test 3 : Lecture avec chargement des commandes (relation 1:N)
     */
    private static void testReadWithCommandes() {
        System.out.println("\n--- Test READ avec COMMANDES ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            // HQL avec JOIN FETCH pour éviter lazy loading exception
            String hql = "FROM Client c LEFT JOIN FETCH c.commandes WHERE c.id = :id";
            Query<Client> query = session.createQuery(hql, Client.class);
            query.setParameter("id", 1L);
            
            Client client = query.uniqueResult();
            
            if (client != null) {
                System.out.println("✓ Client: " + client.getNom());
                System.out.println("  Commandes associées:");
                for (Commande cmd : client.getCommandes()) {
                    System.out.println("    - " + cmd);
                }
            }
            
        } finally {
            session.close();
        }
    }

    /**
     * Variante 2 : Requête HQL avancée - Clients ayant commandé plus de 1000 DH
     */
    private static void testHqlMontantSuperieur() {
        System.out.println("\n--- Test HQL (montant > 1000) ---");
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            String hql = "SELECT c FROM Client c JOIN c.commandes cmd WHERE cmd.montant > :montant";
            Query<Client> query = session.createQuery(hql, Client.class);
            query.setParameter("montant", new BigDecimal("1000"));
            List<Client> clients = query.list();

            if (clients.isEmpty()) {
                System.out.println("✗ Aucun client avec commande > 1000 DH");
            } else {
                System.out.println("✓ Clients avec commande > 1000 DH :");
                clients.forEach(c -> System.out.println("  - " + c));
            }

        } finally {
            session.close();
        }
    }

    /**
     * Variante 3 : API Critères - Emails finissant par emsi.ma
     */
    private static void testCriteriaEmailLike() {
        System.out.println("\n--- Test Criteria (email LIKE %emsi.ma) ---");
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Client> cq = cb.createQuery(Client.class);
            Root<Client> root = cq.from(Client.class);

            cq.select(root).where(cb.like(root.get("email"), "%emsi.ma"));

            List<Client> clients = session.createQuery(cq).getResultList();

            if (clients.isEmpty()) {
                System.out.println("✗ Aucun client avec email se terminant par emsi.ma");
            } else {
                System.out.println("✓ Clients avec email @emsi.ma :");
                clients.forEach(c -> System.out.println("  - " + c));
            }

        } finally {
            session.close();
        }
    }
    
    /**
     * Test 4 : Mise à jour d'un client
     */
    private static void testUpdate() {
        System.out.println("\n--- Test UPDATE ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            // Récupération et modification
            Client client = session.get(Client.class, 1L);
            if (client != null) {
                String oldEmail = client.getEmail();
                client.setEmail("nouveau.email@emsi.ma");
                
                // Pas besoin d'appeler update() : Hibernate détecte les changements
                tx.commit();
                System.out.println("✓ Email modifié: " + oldEmail + " → " + client.getEmail());
            }
            
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("✗ Erreur lors de la mise à jour: " + e.getMessage());
        } finally {
            session.close();
        }
    }
    
    /**
     * Test 5 : Suppression d'une commande
     */
    private static void testDelete() {
        System.out.println("\n--- Test DELETE ---");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            // Suppression de la commande ID=1
            Commande commande = session.get(Commande.class, 1L);
            if (commande != null) {
                session.delete(commande);
                tx.commit();
                System.out.println("✓ Commande supprimée: " + commande.getId());
            }
            
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("✗ Erreur lors de la suppression: " + e.getMessage());
        } finally {
            session.close();
        }
    }
}