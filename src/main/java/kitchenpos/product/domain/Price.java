package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(0);

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
            throw new IllegalArgumentException("가격은 0 이상의 정수만 입력할 수 있습니다.");
        }
    }

    public long getValue() {
        return value;
    }

    public Price add(long value) {
        return new Price(this.value + value);
    }

    public Price add(Price price, long quantity) {
        return add(price.getValue() * quantity);
    }
}
