package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private ProductName name;
    private BigDecimal price;

    private Product() {
    }

    private Product(Long id, ProductName name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(null, ProductName.from(name), price);
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, ProductName.from(name), price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price;
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
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
