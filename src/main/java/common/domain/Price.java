package common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column
    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public Price(long price) {
        this(new BigDecimal(price));
    }

    public Price(Quantity quantity) {
        this(quantity.getQuantity());
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price add(Price operand) {
        return new Price(price.add(operand.getPrice()));
    }

    public Price multiply(Price operand) {
        return new Price(price.multiply(operand.getPrice()));
    }

    public boolean isEqual(Price operand) {
        return price.compareTo(operand.price) == 0;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
