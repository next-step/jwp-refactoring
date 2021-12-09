package kitchenpos.menu.domain.product;

import kitchenpos.menu.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.util.ObjectUtils;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price productPrice;

    protected Product() {
    }

    public Product(String name, BigDecimal productPrice) {
        validation(name, productPrice);
        this.name = name;
        this.productPrice = new Price(productPrice);
    }

    private void validation(String name, BigDecimal price) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("상품명이 존재하지 않습니다");
        }
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("금액이 존재하지 않습니다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getProductPrice() {
        return productPrice;
    }
}
