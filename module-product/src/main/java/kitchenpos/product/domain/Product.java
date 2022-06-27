package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private ProductName name;
    @Embedded
    private ProductPrice price;

    protected Product() {}

    private Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = ProductName.from(name);
        this.price = ProductPrice.from(price);
    }

    private Product(String name, BigDecimal price) {
        this.name = ProductName.from(name);
        this.price = ProductPrice.from(price);
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public String findName() {
        return name.getValue();
    }

    public BigDecimal findPrice() {
        return price.getValue();
    }

    public ProductPrice getPrice() {
        return this.price;
    }
}
