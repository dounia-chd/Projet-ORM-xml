package ma.emsi.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe singleton pour gérer la SessionFactory Hibernate.
 * Initialise la configuration depuis hibernate.cfg.xml.
 */
public class HibernateUtil {
    
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Chargement de hibernate.cfg.xml depuis resources/
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            
            System.out.println("✓ SessionFactory créée avec succès");
            
        } catch (Throwable ex) {
            System.err.println("✗ Échec de création SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Retourne l'instance unique de SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    /**
     * Ferme la SessionFactory (à appeler à la fin de l'application)
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("✓ SessionFactory fermée");
        }
    }
}