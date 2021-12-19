package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public final class Quantity {
    private static final Long MIN_QUANTITY = 1L;
    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(Long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public static Quantity valueOf(Long quantity) {
        return new Quantity(quantity);
    }

    public Long getQuantity() {
        return quantity;
    }

    private void validate(Long quantity) {
        if (quantity == null) {
            throw new InvalidArgumentException("수량은 필수입니다.");
        }
        if (quantity < MIN_QUANTITY) {
            throw new InvalidArgumentException(String.format("수량은 %s 보다 많아야 합니다.", MIN_QUANTITY));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o.getClass().equals(Long.class)) {
            return quantity.equals(o);
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity other = (Quantity) o;
        return quantity.equals(other.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
