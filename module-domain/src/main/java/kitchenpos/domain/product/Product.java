package kitchenpos.domain.product;

import kitchenpos.domain.menu.Price;
import kitchenpos.common.BaseEntity;

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

    protected Product() {
    }

    protected Product(final String name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public static Product of(final String name, final Price price) {
        return new Product(name, price);
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

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Product setPrice(final BigDecimal price) {
        this.price = Price.of(price);
        return this;
    }
}
