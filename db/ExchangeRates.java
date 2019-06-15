package db;

import javax.persistence.*;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    private String first;

    private String second;

    @Column(name="relation", nullable = false)
    private Float relation;

    public ExchangeRates() {}

    public ExchangeRates(String first, String second, Float relation) {
        this.first = first;
        this.second = second;
        this.relation = relation;
    }

    public Float calculate(Float money) throws NullPointerException{
        if(money == null){
            throw new NullPointerException();
        }

        money *= relation;
        System.out.println((Math.round(money * 100.0f) / 100.0f) + "--------");
        return Math.round(money * 100.0f) / 100.0f;
    }

    public long getId() {
        return id;
    }

    public void setId(long er_name) {
        this.id = er_name;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String er_first) {
        this.first = er_first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String er_second) {
        this.second = er_second;
    }

    public Float getRelation() {
        return relation;
    }

    public void setRelation(Float er_relation) {
        this.relation = er_relation;
    }

    @Override
    public String toString() {
        return "ExchangeRate {" +
                "er_name = " + id +
                ", er_first = " + first +
                ", er_second = " + second +
                ", er_relation = " + relation +
                '}';
    }
}
