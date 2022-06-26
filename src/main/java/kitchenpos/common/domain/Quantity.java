package kitchenpos.common.domain;

import static java.util.Objects.requireNonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private final Long quantity;

    // entity 기본생성자 이므로 사용 금지
    protected Quantity() {
        quantity = 0L;
    }

    public Quantity(Long quantity) {
        validateQuantity(quantity);

        this.quantity = quantity;
    }

    private void validateQuantity(Long quantity) {
        requireNonNull(quantity, "수량이 존재하지 않습니다.");
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 양수값 이어야 합니다.");
        }
    }

    public Long getQuantity() {
        return quantity;
    }

}
