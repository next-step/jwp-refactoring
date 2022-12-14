package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private Price price;

    protected Product() {}

    public Product(String name, BigDecimal price) {
        this.name = new ProductName(name);
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public BigDecimal getPrice() {
        return price.value();
    }
}
