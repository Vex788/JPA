package operations;

import db.Account;
import db.Client;
import db.ExchangeRates;
import db.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DBOperations {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    static final Random RND = new Random();
    static final String[] CURRENCY = new String[] {"UAH", "USD", "EUR"};

    public DBOperations(EntityManagerFactory emf, EntityManager em) {
        this.emf = emf;
        this.em = em;
    }

    public void createRandomEntities(Scanner sc) {
        try {
            em.getTransaction().begin();

            System.out.print("Enter clients count: ");
            int count = sc.nextInt();
            Client client;

            for (int i = 0; i < count; i++) {
                client = new Client("name" + (i + 1));
                // add client
                em.persist(client);
                // add client account currency
                em.persist(new Account(client, CURRENCY[RND.nextInt(3)]));
                em.persist(new Account(client, CURRENCY[RND.nextInt(3)]));
            }
            // exchange rates
            em.persist(new ExchangeRates("UAH", "USD", 0.038f));
            em.persist(new ExchangeRates("UAH", "EUR", 0.034f));
            em.persist(new ExchangeRates("USD", "UAH", 26.47f));
            em.persist(new ExchangeRates("USD", "EUR", 0.89f));
            em.persist(new ExchangeRates("EUR", "UAH", 29.74f));
            em.persist(new ExchangeRates("EUR", "USD", 1.12f));

            em.getTransaction().commit(); // save data to a database
        } catch (Exception ex) {
            em.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    public void viewClients() {
        Query query = em.createQuery("SELECT c FROM Client c", Client.class);
        List<Client> list = (List<Client>) query.getResultList();

        for (Client c : list) {
            System.out.println(c);
        }
    }

    public void viewAccounts() {
        Query query = em.createQuery("SELECT c FROM Account c", Account.class);
        List<Account> list = (List<Account>) query.getResultList();

        for (Account c : list) {
            System.out.println(c);
        }
    }

    public void viewExchangeRates() {
        Query query = em.createQuery("SELECT c FROM ExchangeRates c", ExchangeRates.class);
        List<ExchangeRates> list = (List<ExchangeRates>) query.getResultList();

        for (ExchangeRates c : list) {
            System.out.println(c);
        }
    }

    public void viewClientExchangeRates(Scanner sc) {
        System.out.println("Enter account id: ");
        long accountId = sc.nextLong();

        Query query = em.createQuery("SELECT c FROM Account c WHERE c.id = :id", Account.class);
        query.setParameter("id", accountId);

        Account account = (Account) query.getSingleResult();

        Query queryER = em.createQuery("SELECT c FROM ExchangeRates c", ExchangeRates.class);
        List<ExchangeRates> list = (List<ExchangeRates>) queryER.getResultList();

        StringBuilder exchangeRatesString = new StringBuilder();

        for (ExchangeRates exchangeRates : list) {
            if (account.getType().equals(exchangeRates.getFirst())) {
                exchangeRatesString.append(account.getBalance() * exchangeRates.getRelation() + exchangeRates.getSecond() + ", ");
            }
        }

        exchangeRatesString.deleteCharAt(exchangeRatesString.length() - 2);
        exchangeRatesString.deleteCharAt(exchangeRatesString.length() - 1);

        System.out.println("Account {" +
                "id = " + account.getId() +
                ", client = " + account.getClient().getName() +
                ", type = " + account.getType() +
                ", balance = " + account.getBalance() + " (" + exchangeRatesString.toString()+
                ")}");
    }

    public void refill(Scanner sc, Client client) {
        try {
            System.out.println("Enter client id: ");
            long accountId = sc.nextLong();
            System.out.println("Enter value: ");
            Float money = Float.parseFloat(sc.next());

            Query query = em.createQuery("SELECT c FROM Account c WHERE c.id = :id", Account.class);
            query.setParameter("id", accountId);

            Account account = (Account) query.getSingleResult();
            account.addMoney(money);

            Transaction t = new Transaction(client, account, money, false);
            client.addTransaction(t);

            System.out.println("Operation success");
        } catch (NoResultException e) {
            System.err.println("ID doesn't exists");
            return;
        }
    }

    public void wythdrawMoney(Scanner sc, Client client) {
        try {
            System.out.println("Enter client id: ");
            long accountId = sc.nextLong();
            System.out.println("Enter value: ");
            Float money = Float.parseFloat(sc.next());

            Query query = em.createQuery("SELECT c FROM Account c WHERE c.id = :id", Account.class);
            query.setParameter("id", accountId);

            Account account = (Account) query.getSingleResult();
            account.withdrowMoney(money);

            Transaction t = new Transaction(client, account, money, true);
            client.addTransaction(t);

            System.out.println("Operation success");
        } catch (NoResultException e) {
            System.err.println("ID doesn't exists");
            return;
        }
    }

    public void transferMoney(Scanner sc, Client client) throws NoResultException {
        try {
            System.out.println("Enter first client id: ");
            long fId = Long.parseLong(sc.next());

            Query firstQuery = em.createQuery("SELECT acc FROM Account acc WHERE acc.id = :id", Account.class);
            firstQuery.setParameter("id", fId);
            Account firstAccount = (Account) firstQuery.getSingleResult();

            System.out.println("Enter second client id: ");
            long sId = Long.parseLong(sc.next());

            Query secondQuery = em.createQuery("SELECT acc FROM Account acc WHERE acc.id = :id", Account.class);
            secondQuery.setParameter("id", sId);
            Account secondAccount = (Account) secondQuery.getSingleResult();

            System.out.println("Enter value: ");
            String sMoney = sc.next();
            Float money = Float.parseFloat(sMoney);

            firstAccount.withdrowMoney(money);

            ExchangeRates exchangeRates = null;

            if (!firstAccount.getType().equals(secondAccount.getType())) {
                Query rateQuery = em.createQuery("SELECT er FROM ExchangeRates er WHERE er.first = :from AND er.second = :to", ExchangeRates.class);
                rateQuery.setParameter("from", firstAccount.getType());
                rateQuery.setParameter("to", secondAccount.getType());
                exchangeRates = (ExchangeRates) rateQuery.getSingleResult();
                money = exchangeRates.calculate(money);
            }

            secondAccount.addMoney(money);

            Transaction t = new Transaction(client, firstAccount, secondAccount, exchangeRates, money);
            client.addTransaction(t);
            System.out.println("Operation success");
        } catch (NoResultException e) {
            System.err.println("ID doesn't exists");
            return;
        }
    }
}
