package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    private Product(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(long id, String name, BigDecimal price) {
        return new Product(id,name,price);
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
