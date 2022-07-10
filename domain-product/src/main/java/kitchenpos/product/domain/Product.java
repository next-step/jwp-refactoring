package kitchenpos.product.domain;

import kitchenpos.domain.vo.BaseEntity;
import kitchenpos.domain.vo.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    public Product() {
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = new Price(price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public static Product createProduct(final String name, final BigDecimal price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
