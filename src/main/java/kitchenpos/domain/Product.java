package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Name name;
    private Price price;

    protected Product() {
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    public Product(Name name, Price price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this(id, name, new Price(price));
    }

    public Product(Long id, String name, Price price) {
        this(id, new Name(name), price);
    }

    public Product(Long id, Name name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Price multiplyPrice(Quantity quantity) {
        return price.multiply(quantity.toLong());
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
