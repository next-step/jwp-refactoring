package kitchenpos.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(Long quantity) {
        this.quantity = quantity;
    }

    public static Quantity of(Long quantity) {
        return new Quantity(quantity);
    }

    public Long value() {
        return this.quantity;
    }
}
