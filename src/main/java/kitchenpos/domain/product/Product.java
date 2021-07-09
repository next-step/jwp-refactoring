package kitchenpos.domain.product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Name name;
    private Price price;

    // for jpa
    public Product() {
    }

    private Product(Long id, Name name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, Price price) {
        return new Product(null, Name.of(name), price);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Price multiply(BigDecimal factor) {
        return price.multiply(factor);
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
