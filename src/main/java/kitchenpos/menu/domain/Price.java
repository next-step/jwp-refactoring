package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    @Column(name = "price")
    private int value;

    public static final int MIN_PRICE = 0;

    protected Price() {
    }

    private Price(int value) {
        validatePrice(value);
        this.value = value;
    }

    public static Price of(int price) {
        return new Price(price);
    }

    private void validatePrice(int value) {
        if (value < MIN_PRICE) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isLargerThan(long value) {
        return this.value > value;
    }

    public int getValue() {
        return value;
    }
}
