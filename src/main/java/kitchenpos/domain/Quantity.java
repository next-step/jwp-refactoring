package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    private Long quantity;

    protected Quantity() {
    }

    public Quantity(Long quantity) {
        validate(quantity);

        this.quantity = quantity;
    }

    public Long toLong() {
        return quantity;
    }

    private void validate(Long quantity) {
        if (quantity <= 0){
            throw new IllegalArgumentException();
        }
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
