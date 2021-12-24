package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(Long id, String name, BigDecimal money) {
        this.id = id;
        this.name = name;
        this.price = new Price(money);
    }

    public Product(String name, BigDecimal money) {
        this.name = name;
        this.price = new Price(money);
    }

    public Price multiply(Long quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
