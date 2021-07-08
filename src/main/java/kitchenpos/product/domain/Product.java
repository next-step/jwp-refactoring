package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.product.exception.ProductPriceNegativeException;
import kitchenpos.product.exception.ProductPriceEmptyException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(String name, BigDecimal price) {
        validationPrice(price);
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validationPrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new ProductPriceEmptyException();
        }
        if (isNegativeNumber(price)) {
            throw new ProductPriceNegativeException();
        }
    }

    private boolean isNegativeNumber(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
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
}
