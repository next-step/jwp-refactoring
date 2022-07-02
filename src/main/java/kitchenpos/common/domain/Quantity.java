package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    public static final Long MIN_QUANTITY = 0L;

    @Column(nullable = false)
    private Long element;

    protected Quantity() {
    }

    public Quantity(Long element) {
        validate(element);
        this.element = element;
    }

    private void validate(Long quantity) {
        if (Objects.isNull(quantity) || quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("수량 0 이상이어야 합니다.");
        }
    }

    public Long get() {
        return element;
    }
}
