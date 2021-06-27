package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private final int value;

    protected Price() {
        this.value = 0;
    }

    public Price(int value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
    }
}
