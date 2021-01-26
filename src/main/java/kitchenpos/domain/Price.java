package kitchenpos.domain;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.advice.exception.PriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<Price> {

    @Column(name = "price", nullable = false)
    private BigDecimal money;

    protected Price() {
    }

    public Price(BigDecimal money) {
        validateEmptyPrice(money);
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public BigDecimal multiply(long quantity) {
        return this.money.multiply(BigDecimal.valueOf(quantity));
    }

    private void validateEmptyPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceException("가격이 0보다 작을 수 없습니다", price.longValue());
        }
    }

    @Override
    public int compareTo(Price price) {
        return this.money.compareTo(price.getMoney());
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + money +
                '}';
    }
}
