package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    @Column(name = "price")
    private BigDecimal price;

    public Price() {
    }

    public Price(final BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        if (null == price || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0 이상의 숫자여야 합니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
