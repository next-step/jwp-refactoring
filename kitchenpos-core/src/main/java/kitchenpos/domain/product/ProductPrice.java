package kitchenpos.domain.product;

import kitchenpos.domain.product.exception.IllegalProductPriceException;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductPrice {
    private static final String EMPTY_PRICE_ERROR_MESSAGE = "가격은 빈값일 수 없습니다.";
    private static final String LESS_THEN_ZERO_ERROR_MESSAGE = "가격은 0보다 작을 수 업습니다.";
    private BigDecimal price;

    protected ProductPrice() {
    }

    private ProductPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalProductPriceException(EMPTY_PRICE_ERROR_MESSAGE);
        }
        if (isLessThenZero(price)) {
            throw new IllegalProductPriceException(LESS_THEN_ZERO_ERROR_MESSAGE);
        }
    }

    private boolean isLessThenZero(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public static ProductPrice of(BigDecimal price) {
        return new ProductPrice(price);
    }

    public static ProductPrice of(int price) {
        return new ProductPrice(BigDecimal.valueOf(price));
    }

    public boolean matchPrice(BigDecimal targetPrice) {
        return price.compareTo(targetPrice) == 0;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
