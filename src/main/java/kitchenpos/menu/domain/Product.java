package kitchenpos.menu.domain;

import io.micrometer.core.instrument.util.StringUtils;

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

    protected Product() {
    }

    private Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
    }

    public static Product of(Long id, String name, BigDecimal price) {
        validateProductName(name);
        return new Product(id, name, price);
    }

    private static void validateProductName(String name) {
        if(StringUtils.isBlank(name)){
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    public BigDecimal calculate(long quantity) {
        return this.price.multiply(BigDecimal.valueOf(quantity)).getValue();
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price.getValue();
    }
}
