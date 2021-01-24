package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    private Product(Long id, String name, ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, new ProductPrice(price));
    }

    public BigDecimal multiplyQuantity(BigDecimal quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ProductPrice getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
