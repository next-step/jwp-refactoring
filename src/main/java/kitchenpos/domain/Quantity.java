package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    @Column(name = "qualtity")
    private long value;

    protected Quantity() {
    }

    public Quantity(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
