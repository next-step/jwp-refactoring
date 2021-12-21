package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final BigDecimal MIN = BigDecimal.ZERO;
    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price")
    private BigDecimal price;

    public static Price of(int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price of(BigDecimal price, BigDecimal lessThanPrice) {
        checkPriceGreaterThanMin(price, lessThanPrice);
        return new Price(price);
    }

    protected Price() {
    }

    public Price(BigDecimal price) {
        validMin(price);

        this.price = price;
    }

    public BigDecimal value() {
        return price;
    }

    private void validMin(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private static void checkPriceGreaterThanMin(BigDecimal price, BigDecimal lessThanPrice) {
        if (price.compareTo(lessThanPrice) > 0) {
            throw new InvalidParameterException("가격이 큼");
        }
    }
}
