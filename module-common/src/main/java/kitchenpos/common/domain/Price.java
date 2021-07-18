package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.common.exception.PriceEmptyException;
import kitchenpos.common.exception.PriceNegativeException;

public class Price implements Comparable<Price> {

    private final BigDecimal amount;

    public Price() {
        this(new BigDecimal(0));
    }

    public Price(long amount) {
        this(BigDecimal.valueOf(amount));
    }

    public Price(BigDecimal amount) {
        validationPrice(amount);
        this.amount = amount;
    }

    public static Price wonOf(long amount) {
        return wonOf(BigDecimal.valueOf(amount));
    }

    public static Price wonOf(BigDecimal amount) {
        return new Price(amount);
    }

    private static void validationPrice(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new PriceEmptyException();
        }
        if (isNegativeNumber(price)) {
            throw new PriceNegativeException();
        }
    }

    private static boolean isNegativeNumber(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Price plus(Price price) {
        return Price.wonOf(this.amount.add(price.amount));
    }

    public Price multiply(Quantity quantity) {
        BigDecimal value = BigDecimal.valueOf(quantity.getValue());
        return new Price(amount.multiply(value));
    }

    public boolean isExceed(Price sumPrice) {
        return compareTo(sumPrice) > 0;
    }

    @Override
    public int compareTo(Price price) {
        return amount.compareTo(price.amount);
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
        return Objects.equals(amount, price.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }
}
