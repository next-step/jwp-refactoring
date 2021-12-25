package kitchenpos.menu.domain;

import kitchenpos.menu.application.exception.InvalidPrice;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    @Column(name = "price")
    private BigDecimal money;

    protected Price() {
    }

    public Price(BigDecimal money) {
        if (Objects.isNull(money) || money.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPrice("가격을 확인해 주세요.");
        }
        this.money = money;
    }

    public static Price zero() {
        return new Price(BigDecimal.ZERO);
    }

    public Price sum(Price price) {
        return new Price(money.add(price.getMoney()));
    }

    public Price multiply(Long quantity) {
        return new Price(money.multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean isExpensiveThan(BigDecimal value) {
        return money.compareTo(value) >= 0;
    }

    private void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(money, price.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
