package kitchenpos.domain.menu;

import kitchenpos.domain.menu.exceptions.InvalidMenuPriceException;

import java.math.BigDecimal;
import java.util.Objects;

public class MenuPrice {
    private final BigDecimal value;

    public MenuPrice(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException("가격은 음수일 수 없습니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(value, menuPrice.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "MenuPrice{" +
                "value=" + value +
                '}';
    }
}
