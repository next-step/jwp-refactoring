package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {
    private Long quantity;

    protected MenuProductQuantity() {
    }

    public MenuProductQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long toLong() {
        return quantity;
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(this.quantity);
    }
}
