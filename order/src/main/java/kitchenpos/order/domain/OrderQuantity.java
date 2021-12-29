package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderQuantity {

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected OrderQuantity() {

    }

    public OrderQuantity(Long quantity) {
        validateNegativeQuantity(quantity);
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    private void validateNegativeQuantity(Long quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("수량이 음수일 수 없습니다.");
        }
    }
}
