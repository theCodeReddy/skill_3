package klu.hibernate_crud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class App {

    public static void main(String[] args) {

        // Load Hibernate configuration
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        // Register entity classes
        cfg.addAnnotatedClass(Product.class);

        // Build SessionFactory
        SessionFactory factory = cfg.buildSessionFactory();

        // Open session
        Session session = factory.openSession();

        // Begin transaction
        Transaction tx = session.beginTransaction();

        // Create Product object
        Product product = new Product(
                "Laptop",
                "High performance gaming laptop",
                75000.00,
                10
        );

        // Save product (table created automatically if not exists)
        session.persist(product);

        // Commit transaction
        tx.commit();

        // Close resources
        session.close();
        factory.close();

        System.out.println("Product record inserted successfully!");
    }
}
