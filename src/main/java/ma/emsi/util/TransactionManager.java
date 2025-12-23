package ma.emsi.util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;

/**
 * Gestionnaire de transactions simplifiant le pattern try/commit/rollback.
 */
public class TransactionManager {

    /**
     * Exécute une fonction dans une transaction Hibernate et gère commit/rollback.
     *
     * @param action logique métier à exécuter avec la session
     * @param <T>    type de retour de l'action
     * @return résultat de l'action
     */
    public static <T> T executeInTransaction(Function<Session, T> action) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Transaction échouée", e);
        } finally {
            session.close();
        }
    }
}

