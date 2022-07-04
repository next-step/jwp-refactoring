package kitchenpos.global;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validPrice(price);
        this.value = price;
    }

    private void validPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("잘못된 금액을 입력하였습니다.");
        }
    }

    public Price add(Price totalPrice) {
        BigDecimal result = this.value.add(totalPrice.getValue());
        return new Price(result);
    }

    public BigDecimal getValue() {
        return this.value;
    }
}
