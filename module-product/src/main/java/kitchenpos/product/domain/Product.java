package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(Name name, Price price) {
        validate(name, price);
        this.name = name;
        this.price = price;
    }

    public Product(Long id, Name name, Price price) {
        validate(name, price);
        this.id = id;
        this.name = name;
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

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return this.price.getPrice();
    }

    public Name getName() {
        return this.name;
    }

    private void validate(Name name, Price price) {
        validateNullName(name);
        validateNullPrice(price);
    }

    private void validateNullName(Name name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNullPrice(Price price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException();
        }
    }
}
