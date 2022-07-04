package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    @Column(name = "price")
    private Long price;

    public Price() {
    }

    public Price(final Long price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(final Long price) {
        if (null == price || price < 0) {
            throw new IllegalArgumentException("가격은 0 이상의 숫자여야 합니다.");
        }
    }

    public Long value() {
        return price;
    }
}
