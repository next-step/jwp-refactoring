package kitchenpos.menu.domain;

import kitchenpos.product.exception.ProductException;
import kitchenpos.product.exception.ProductExceptionType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected MenuPrice(){}

    private MenuPrice(final BigDecimal value) {
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (value == null || MIN_PRICE.compareTo(value) >= BigDecimal.ZERO.intValue()) {
            throw new ProductException(ProductExceptionType.MIN_PRICE);
        }
    }

    public static MenuPrice of(final BigDecimal value) {
        return new MenuPrice(value);
    }

    public void compareSumPrice(final BigDecimal sum) {
        if (value.compareTo(sum) > BigDecimal.ZERO.intValue()) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MenuPrice{" +
                "value=" + value +
                '}';
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
}
