package kitchenpos.menu.domain;

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

    @Column(nullable = false)
    private BigDecimal price;

    protected Product() {}

    private Product(String name, BigDecimal price) {
        isValidPrice(price);

        this.name = name;
        this.price = price;
    }

    private void isValidPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new WrongPriceException();
        }
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
        return price;
    }

    public void createId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice(Long quantity) {
        return this.price.multiply(BigDecimal.valueOf(quantity));
    }
}
