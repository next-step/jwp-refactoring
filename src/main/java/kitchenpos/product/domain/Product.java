package kitchenpos.product.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(name = "name", nullable = false)
    private Name name;

    @Embedded
    @Column(name = "price", nullable = false)
    private Price price;

    protected Product() {

    }

    public Product(Long id, String name, BigDecimal price) {
        this(name, price);
        this.id = id;
    }

    public Product(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return this.price;
    }

    public BigDecimal getAmount() {
        return this.price.getPrice();
    }
}
