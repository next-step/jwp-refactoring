package kitchenpos.common.domain;

import static kitchenpos.exception.ErrorCode.PRICE_IS_NULL_OR_MINUS;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class Price {
    @Column
    private BigDecimal price;

    public Price() {
    }

    private Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public static Price of(BigDecimal price){
        return new Price(price);
    }

    private void validatePrice(BigDecimal price){
        if(validatePriceNull(price) || validatePriceLessThanZero(price)){
            throw new KitchenposException(PRICE_IS_NULL_OR_MINUS);
        }
    }

    private boolean validatePriceNull(BigDecimal price){
        return Objects.isNull(price);
    }

    private boolean validatePriceLessThanZero(BigDecimal price){
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
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
