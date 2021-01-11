package kitchenpos.domain.product;

import kitchenpos.domain.product.exceptions.InvalidProductPriceException;

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

    protected Product() {
    }

    public Product(final String name, final BigDecimal price) {
        validate(price);
        this.name = name;
        this.price = new ProductPrice(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
        }
    }
}
