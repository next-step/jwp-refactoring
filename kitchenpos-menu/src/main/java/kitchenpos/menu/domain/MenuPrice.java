package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductPrice;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static kitchenpos.common.constants.ErrorCodeType.PRICE_NOT_NULL_AND_ZERO;

@Embeddable
public class MenuPrice {

    @Column(nullable = false)
    private BigDecimal price;

    protected MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
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

    public boolean isBiggerThen(ProductPrice productPrice) {
        return this.price.compareTo(productPrice.getPrice()) > 0;
    }
}
