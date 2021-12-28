package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.CommonErrorCode;
import kitchenpos.exception.InvalidParameterException;

@Embeddable
public class Price {

    private static final BigDecimal MIN = BigDecimal.ZERO;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validateMinPrice(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public BigDecimal value() {
        return price;
    }

    public BigDecimal calculatePrice(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    private void validateMinPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN) < 0) {
            throw new InvalidParameterException(CommonErrorCode.NOT_EMPTY);
        }
    }
}
