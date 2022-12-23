package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.common.constants.ErrorCodeType.PRICE_NOT_NULL_AND_ZERO;

@Embeddable
public class ProductPrice {

    @Column(nullable = false)
    private BigDecimal price;

    protected ProductPrice() {}

    public ProductPrice(BigDecimal price) {
        validCheckPrice(price);
        this.price = price;
    }

    private void validCheckPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(PRICE_NOT_NULL_AND_ZERO.getMessage());
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
