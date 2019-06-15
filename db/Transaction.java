package db;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account fAccount;

    @OneToOne
    @JoinColumn(name = "account_id", insertable= false, updatable= false)
    private Account sAccount;

    @OneToOne
    @JoinColumn(name = "exchange_rates_id")
    private ExchangeRates exchangeRates;

    private Float money;

    private Boolean withdrow;

    private Date time;

    public Transaction() {}

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

    public Account getfEr_fAccount() {
        return fAccount;
    }

    public void setfEr_fAccount(Account fAccount) {
        this.fAccount = fAccount;
    }

    public Account getsEr_sAccount() {
        return sAccount;
    }

    public void setsEr_sAccount(Account sAccount) {
        this.sAccount = sAccount;
    }

    public ExchangeRates getExchangeRate() {
        return exchangeRates;
    }

    public void setExchangeRate(ExchangeRates exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Boolean getWithdrow() {
        return withdrow;
    }

    public void setWithdrow(Boolean withdrow) {
        this.withdrow = withdrow;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Transaction(Client client, Account fAccount, Account sAccount, ExchangeRates exchangeRates, Float money){
        this.client = client;
        this.fAccount = fAccount;
        this.sAccount = sAccount;
        this.exchangeRates = exchangeRates;
        this.money = money;
        this.time = new Date();
    }

    public Transaction(Client client, Account fAccount, Float money, Boolean withdrow){
        this.client = client;
        this.fAccount = fAccount;
        this.money = money;
        this.withdrow = withdrow;
        this.time = new Date();
    }

    @Override
    public String toString() {
        return "Transaction {" +
                "id = " + id +
                ", client = " + client +
                ", fAccount = " + fAccount +
                ", sAccount = " + sAccount +
                ", exchangeRates = " + exchangeRates +
                ", money = " + money +
                ", withdrow = " + withdrow +
                ", time = " + time +
                '}';
    }
}
