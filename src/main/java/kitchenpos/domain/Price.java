package kitchenpos.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class Price {
    @Column(name = "price")
    BigDecimal value;

    protected Price() {
    }

    private Price(int value) {
        validate(value);

        this.value = BigDecimal.valueOf(value).setScale(2);
    }

    private void validate(int value) {
        if (value < 0) {
            throw new InvalidPriceException();
        }
    }

    public static Price of(int value) {
        return new Price(value);
    }

    public static Price of(BigDecimal value) {
        return new Price(value.intValue());
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
