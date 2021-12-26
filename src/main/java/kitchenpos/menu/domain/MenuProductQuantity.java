package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected MenuProductQuantity() {

    }

    public MenuProductQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}
