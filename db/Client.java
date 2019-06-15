package db;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Account> accounts;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Transaction> transaction;

    public Client() {}

    public Client(String name) {
        this.name = name;
        accounts = new ArrayList<>();
        transaction = new ArrayList<>();
    }

    public void addAccount(Account account){
        this.accounts.add(account);
    }

    public void noteTransaction(Transaction transaction){
        this.transaction.add(transaction);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void addTransaction(Transaction transaction) {
        this.transaction.add(transaction);
    }

    @Override
    public String toString() {
        return "Client {" +
                "id = " + id +
                ", name = " + name +
                ", accounts = " + accounts +
                ", transaction = " + transaction +
                '}';
    }
}
