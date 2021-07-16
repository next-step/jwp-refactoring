package kitchenpos.common.domain;


import kitchenpos.common.Exception.WrongPriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final int ZERO = 0;
    private BigDecimal price = BigDecimal.ZERO;

    public Price() {
    }

    public Price(BigDecimal price) {
        validateCheck(price);
        this.price = price;
    }

    private void validateCheck(BigDecimal price) {
        if (Objects.isNull(price) || isMinus(price) ) {
            throw new IllegalArgumentException("가격이 없거나 음수인 상품은 등록할 수 없습니다.");
        }
    }

    private boolean isMinus(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < ZERO;
    }

    public BigDecimal value() {
        return price;
    }

    public void availablePriceCheck(BigDecimal totalPrice) {
        if (price.compareTo(totalPrice) > ZERO) {
            throw new WrongPriceException("메뉴 가격은 상품 가격의 총 합보다 클 수 없습니다.");
        }
    }
}
