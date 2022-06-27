package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column
    private BigDecimal price;

    protected Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal source) {
        if (Objects.isNull(source) || source.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean bigger(BigDecimal target) {
        return price.compareTo(target) > 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void changePrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
