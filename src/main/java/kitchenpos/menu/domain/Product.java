package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private ProductPrice price;

    protected Product() {}

    public Product(String name, Integer price) {
        validate(name);
        this.name = name;
        this.price = new ProductPrice(price);
    }

    private void validate(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.PRODUCT_REQUIRED_NAME.getMessage());
        }
    }

    public BigDecimal calculateAmount(long quantity) {
        return this.price.multiply(new BigDecimal(quantity));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
