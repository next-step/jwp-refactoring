package kitchenpos.menu.domain;

import io.micrometer.core.instrument.util.StringUtils;
import kitchenpos.menu.exception.ProductExceptionCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Embedded
    private ProductPrice price;

    protected Product() {}

    public Product(String name, BigDecimal price) {
        validate(name);

        this.name = name;
        this.price = new ProductPrice(price);
    }

    private void validate(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(ProductExceptionCode.REQUIRED_NAME.getMessage());
        }
    }

    public BigDecimal calculateAmount(long quantity) {
        return this.price.multiply(new BigDecimal(quantity));
    }

    public boolean equalsId(Long productId) {
        return this.id.equals(productId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
