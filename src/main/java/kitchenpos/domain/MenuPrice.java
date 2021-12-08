package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class MenuPrice {

    private static final int MIN_PRICE = 0;

    private final BigDecimal price;

    public MenuPrice(BigDecimal price) {
        check(price);
        this.price = price;
    }

    public MenuPrice(int price) {
        this(new BigDecimal(price));
    }

    private void check(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MIN_PRICE) {
            throw new IllegalArgumentException();
        }
    }
}
