package kitchenpos.common.valueobject;

import kitchenpos.common.valueobject.exception.NegativeQuantityException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    static final long MIN_QUANTITY = 0;

    @Column(name = "quantity")
    private final long value;

    protected Quantity() {
        this.value = 0L;
    }

    private Quantity(long value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(long value) {
        if(value < MIN_QUANTITY){
            throw new NegativeQuantityException(MIN_QUANTITY);
        }
    }

    public static Quantity of(long value){
        return new Quantity(value);
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
