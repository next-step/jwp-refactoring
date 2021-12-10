package kitchenpos.domain;

import java.math.BigDecimal;

public class Price {
    BigDecimal value;

    protected Price() {
    }

    private Price(int value) {
        validate(value);

        this.value = BigDecimal.valueOf(value).setScale(2);
    }

    private void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Price of(int value) {
        return new Price(value);
    }

    public int value() {
        return this.value.intValue();
    }

    public int compareTo(Price comparingValue) {
        return this.value.compareTo(comparingValue.value);
    }

    public Price multiply(long quantity) {
        return new Price((int)(this.value() * quantity));
    }

    public Price add(Price price) {
        return new Price(this.value() + price.value());
    }
}
