package kitchenpos.product.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 미만의 값으로 설정할 수 없습니다.");
        }
    }
}
