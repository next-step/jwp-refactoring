package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.ErrorMessage;

@Embeddable
public class Price implements Comparable<Price>{
    private static String PROPERTY_NAME = "가격";
    private static BigDecimal ZERO_VALUE = BigDecimal.valueOf(0);

    public static Price ZERO = Price.of(ZERO_VALUE);

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {}

    private Price(BigDecimal price) {
        validatePriceIsNull(price);
        validatePriceIsNegative(price);
        this.value = price;
    }

    public static Price of(BigDecimal price){
        return new Price(price);
    }

    private void validatePriceIsNegative(BigDecimal price) {
        if (price.compareTo(ZERO_VALUE)<0){
            throw new IllegalArgumentException(ErrorMessage.cannotBeNegative(PROPERTY_NAME));
        }

    }
    private void validatePriceIsNull(BigDecimal price) {
        if(price == null) {
            throw new IllegalArgumentException(ErrorMessage.cannotBeNull(PROPERTY_NAME));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    @Override
    public int compareTo(Price price) {
        return this.value.compareTo(price.value);
    }

    public BigDecimal value() {
        return value;
    }

    public Price multiply(Quantity quantity) {
        return Price.of(this.value.multiply(BigDecimal.valueOf(quantity.value())));
    }

    public Price add(Price price){
        return Price.of(this.value.add(price.value()));
    }

}
