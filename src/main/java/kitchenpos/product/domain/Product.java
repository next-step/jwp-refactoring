package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private ProductPrice price = new ProductPrice();

    protected Product() {}

    public Product(Long id, ProductPrice price, String name) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(ProductPrice price, String name) {
        this.price = price;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public BigDecimal getCalculateMultiplyQuantity(long quantity) {
        return getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
