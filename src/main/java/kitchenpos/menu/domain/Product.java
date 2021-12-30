package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import kitchenpos.menu.exception.WrongPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {}

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.price = new Price(price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public static Product of(String name, int price) {
        return new Product(name, BigDecimal.valueOf(price));
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public static Product of(Long id, String name, int price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
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

    public void createId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice(Long quantity) {
        Price multiplyPrice = this.price.multiply(quantity);
        return multiplyPrice.getPrice();
    }
}
