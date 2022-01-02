package menu.domain;

import kitchenpos.common.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

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

    public BigDecimal getTotalPrice(Long quantity) {
        Price multiplyPrice = this.price.multiply(quantity);
        return multiplyPrice.getPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal toBigDecimal() {
        return price.getPrice();
    }

}
