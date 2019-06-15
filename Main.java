import db.Account;
import db.Client;
import operations.DBOperations;

import javax.persistence.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Bank");
            EntityManager em = emf.createEntityManager();

            DBOperations operations = new DBOperations(emf, em);

            operations.createRandomEntities(sc); // create clients

            // login
            Client client = new Client("Client");

            try {
                em.getTransaction().begin();
                em.persist(client);
                em.persist(new Account(client, "UAH"));
                em.persist(new Account(client, "USD"));
                em.persist(new Account(client, "EUR"));

                em.getTransaction().commit(); // save data to a database
            } catch (Exception ex) {
                em.getTransaction().rollback();
                ex.printStackTrace();
            }
            // end login
            try {
                while (true) {
                    System.out.println("1: show all clients");
                    System.out.println("2: show all accounts");
                    System.out.println("3: show all exchange rates");
                    System.out.println("4: show account data");
                    System.out.println("5: refill account");
                    System.out.println("6: withdraw money");
                    System.out.println("7: transfer money");

                    System.out.print("-> ");

                    String s = sc.next();

                    switch (s) {
                        case "1":
                            operations.viewClients();
                            break;
                        case "2":
                            operations.viewAccounts();
                            break;
                        case "3":
                            operations.viewExchangeRates();
                            break;
                        case "4":
                            operations.viewClientExchangeRates(sc);
                            break;
                        case "5":
                            operations.refill(sc, client);
                            break;
                        case "6":
                            operations.wythdrawMoney(sc, client);
                            break;
                        case "7":
                            operations.transferMoney(sc, client);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
}
