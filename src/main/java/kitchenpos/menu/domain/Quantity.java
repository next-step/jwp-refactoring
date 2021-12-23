package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalQuantityException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * packageName : kitchenpos.domain
 * fileName : Quantity
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
@Embeddable
public class Quantity {
    @Transient
    public static final Long MIN_QUANTITY = 0L;

    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    public Quantity(Long value) {
        validate(value);
        this.quantity = value;
    }

    private void validate(Long value) {
        if (Objects.isNull(value) || value < MIN_QUANTITY) {
            throw new IllegalQuantityException();
        }
    }

    public static Quantity of(Long value) {
        return new Quantity(value);
    }

    public Long value() {
        return quantity;
    }
}
