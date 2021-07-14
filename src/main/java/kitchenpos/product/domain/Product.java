package kitchenpos.product.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;

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

    public Product() {}

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Product(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
