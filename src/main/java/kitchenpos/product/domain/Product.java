package kitchenpos.product.domain;

import common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    private static final String INVALID_PRICE = "유요하지 않은 가격입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Price price;

    protected Product() {

    }

    public Product(String name, int price) {
        this.name = name;
        this.price = new Price(price);
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

}
