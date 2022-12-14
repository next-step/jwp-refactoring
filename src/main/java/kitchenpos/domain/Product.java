package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product() {
    }

    public static Product of(String name, BigDecimal price) {
        validNameLength(name);
        validMinusPrice(price);
        return new Product(name, price);
    }

    private static void validNameLength(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("이름은 빈 값이 아니어야 합니다");
        }
    }

    private static void validMinusPrice(BigDecimal price) {
        if (price.intValue() < 0) {
            throw new IllegalArgumentException("가격은 0이상 이어야 합니다");
        }
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
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(price.intValue(), product.price.intValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
