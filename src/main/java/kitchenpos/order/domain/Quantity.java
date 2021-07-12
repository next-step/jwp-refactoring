package kitchenpos.order.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private Long quantity;

    private Quantity() {
    }

    private Quantity(Long quantity) {
        this.quantity = quantity;
    }

    public static Quantity of(Long quantity) {
        return new Quantity(quantity);
    }

    public Long getValue(){
        return this.quantity;
    }
}
