package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Price {
    private static final Long PRICE_MIN_VALUE = 0L;
    private static final String INVALID_PRICE_EXCEPTION = "가격은 0보다 작을 수 없습니다.";

    @Column(name = "price", nullable = false)
    private Long price;

    protected Price() {

    }

    private Price(Long price) {
        validate(price);
        this.price = price;
    }

    public static Price of(Long price) {
        return new Price(price);
    }

    private void validate(Long price) {
        if (Objects.isNull(price) || price.compareTo(PRICE_MIN_VALUE) < 0) {
            throw new InvalidPriceException(INVALID_PRICE_EXCEPTION);
        }
    }

    public Long getPrice() {
        return price;
    }

    public Price multiply(Long quantity) {
        return Price.of(price * quantity);
    }

    public Price sum(Price price) {
        return Price.of(this.price + price.getPrice());
    }

    public static Price zero() {
        return new Price(0L);
    }
}

