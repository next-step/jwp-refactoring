package kitchenpos.core.product.domain;

import javax.persistence.Embedded;
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
    @Embedded
    private ProductName name;
    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    private Product(String name, BigDecimal price) {
        this.name = ProductName.of(name);
        this.price = ProductPrice.of(price);
    }

    public Product(Long id, String name, int price) {
        this.id = id;
        this.name = ProductName.of(name);
        this.price = ProductPrice.of(price);
    }

    public static Product of(String name, int price) {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public static Product generate(Long id, String name, int price) {
        return new Product(id, name, price);
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

    public boolean matchName(String targetName) {
        return this.name.matchName(targetName);
    }

    public boolean matchPrice(int targetPrice) {
        return this.price.matchPrice(BigDecimal.valueOf(targetPrice));
    }
}
