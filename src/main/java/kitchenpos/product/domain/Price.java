package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private static final Integer MIN = 0;
    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    protected Price(BigDecimal price) {
        validate(price);
        this.value = price;
    }

    public static Price from(Integer price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MIN) {
            throw new IllegalArgumentException();
        }
    }

    public boolean biggerThan(Price target) {
        return value.compareTo(target.value) > 0;
    }

    public Price add(Price price) {
        return new Price(this.value.add(price.value));
    }

    public static Price multiply(Product product, long quantity) {
        return Price.from(
                product.getPriceBigDecimal()
                        .multiply(BigDecimal.valueOf(quantity))
        );
    }

    public BigDecimal getValue() {
        return value;
    }

    public Integer getPriceValue() {
        return value.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(getValue(), price.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
