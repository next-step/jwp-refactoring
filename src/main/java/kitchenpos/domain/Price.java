package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final BigDecimal PRICE_MIN_VALUE = BigDecimal.ZERO;
    private static final String INVALID_PRICE_EXCEPTION = "가격은 0보다 작을 수 없습니다.";

    @Column(name = "price")
    private BigDecimal price;

    protected Price() {

    }

    private Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    private void validate(BigDecimal price) {
        if(Objects.isNull(price) || price.compareTo(PRICE_MIN_VALUE) < 0) {
            throw new InvalidPriceException(INVALID_PRICE_EXCEPTION);
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price multiply(Long quantity) {
        return Price.of(price.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price sum(Price price) {
        return Price.of(this.price.add(price.getPrice()));
    }

    public static Price zero() {
        return new Price(BigDecimal.ZERO);
    }
}

