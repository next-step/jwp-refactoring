package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private final long value;

    protected Price() {
        this.value = 0;
    }

    public Price(int value) {
        this(Long.valueOf(value));
    }

    public Price(Long value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public long getValue() {
        return value;
    }

    public Price add(long value) {
        return new Price(this.value + value);
    }
}
