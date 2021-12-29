package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativeQuantityException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {

    private static final Long MINIMUM = 1L;

    private Long quantity;

    public Quantity() {
    }

    private Quantity(final Long quantity) {
        this.quantity = quantity;
    }

    public static Quantity from(final Long quantity) {
        validate(quantity);
        return new Quantity(quantity);
    }

    private static void validate(final Long quantity) {
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Quantity quantity1 = (Quantity) o;

        return quantity.equals(quantity1.quantity);
    }

    @Override
    public int hashCode() {
        return quantity.hashCode();
    }
}
