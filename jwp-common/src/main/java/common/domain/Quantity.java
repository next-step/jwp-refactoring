package common.domain;

import common.exception.NegativeQuantityException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {

    private static final Long MINIMUM = 1L;

    @Column
    private Long quantity;

    public Quantity() {
    }

    private Quantity(final Long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(final Long quantity) {
        return new Quantity(quantity);
    }

    private void validate(final Long quantity) {
        if (Objects.isNull(quantity) || quantity < MINIMUM) {
            throw new NegativeQuantityException();
        }
    }

    public static Quantity valueOf(final Long quantity) {
        return from(quantity);
    }

    public Long toLong() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return Objects.equals(quantity, quantity1.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
