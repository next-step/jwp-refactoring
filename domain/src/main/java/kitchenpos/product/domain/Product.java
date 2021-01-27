package kitchenpos.product.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product extends BaseEntity {
    @Column
    private String name;
    @Embedded
    private ProductPrice price;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = new ProductPrice(price);
    }

    protected Product() {
    }

    public BigDecimal getSumOfProducts(Long quantity) {
        return price.multiply(quantity);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}
