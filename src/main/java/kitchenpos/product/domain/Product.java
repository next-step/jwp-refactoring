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
    private Name name;
    private BigDecimal price;

    public Product() {
    }

    private Product(String name, BigDecimal price) {
        this.name = Name.of(name);
        this.price = price;
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = Name.of(name);
        this.price = price;
    }

    public static Product of(String name, Integer price) {
        if (price == null) {
            throw new IllegalArgumentException();
        }
        return new Product(name, BigDecimal.valueOf(price));
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = Name.of(name);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}
