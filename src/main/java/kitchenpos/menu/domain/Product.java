package kitchenpos.menu.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.common.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Price price;

    public Product() {}

    public Product(String name, BigDecimal price) {
        validation(name);

        this.name = name;
        this.price = new Price(price);
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

    private void validation(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_PRODUCT_NAME.getErrorMessage());
        }
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

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public void setPrice(BigDecimal price) {
    }

    public BigDecimal calculateAmount(long quantity) {
        return this.price.multiply(new BigDecimal(quantity));
    }
}
