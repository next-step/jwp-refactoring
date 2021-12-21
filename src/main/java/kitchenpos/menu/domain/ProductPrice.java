package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductPrice {
    private BigDecimal price;

    protected ProductPrice() {
    }

    private ProductPrice(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("가격은 빈값일 수 없습니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 업습니다.");
        }
    }

    public static ProductPrice of(BigDecimal price) {
        return new ProductPrice(price);
    }

    public boolean matchPrice(BigDecimal targetPrice) {
        return price.compareTo(targetPrice) == 0;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
