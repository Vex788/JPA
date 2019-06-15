package db;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name="type", nullable = false)
    private String type;

    @Column(name="balance", nullable = false)
    private Float balance;

    public Account() {}

    public Account(Client client, String type) {
        this.client = client;
        this.type = type;
        this.balance = 0.0f;

        client.addAccount(this);
    }

    public synchronized boolean checkBalance(Float money){
        if(balance >= money) {
            return true;
        }

        return false;
    }

    public synchronized void addMoney(Float money) throws NullPointerException{
        if(money == null){
            throw new NullPointerException();
        }

        this.balance += money;
    }

    public synchronized void withdrowMoney(Float money) throws NullPointerException{
        if(money == null){
            throw new NullPointerException();
        }

        this.balance -= money;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account {" +
                "id = " + id +
                ", client = " + client.getName() +
                ", type = " + type +
                ", balance = " + balance +
                '}';
    }
}
