package kitchenpos.common.model;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    public static final Price ZERO = Price.of(BigDecimal.ZERO);
    private static final String INVALID_PRICE = "올바르지 않은 금액입니다 : ";
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal amount) {
        this.price = validate(amount);
    }

    public Price times(Long quantity) {
        return new Price(price.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price add(Price price) {
        return new Price(this.price.add(price.getPrice()));
    }

    private BigDecimal validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(INVALID_PRICE + price);
        }
        return price;
    }

    public static Price of(BigDecimal amount) {
        return new Price(amount);
    }

    public boolean isBigger(BigDecimal price) {
        return this.price.compareTo(price) < 0;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
