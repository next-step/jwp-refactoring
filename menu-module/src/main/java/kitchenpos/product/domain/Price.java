package kitchenpos.product.domain;

import kitchenpos.exception.IllegalPriceException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private static final String NULL_OR_NEGATIVE_NUMBER_ERROR_MESSAGE = "상품 가격은 음수 일 수 없습니다.";

    private BigDecimal price;

    public Price() {
    }

    public Price(int price) {
        this(new BigDecimal(price));
    }

    public Price(BigDecimal price) {
        checkPriceNullOrNegative(price);
        this.price = price;
    }

    public BigDecimal price() {
        return price;
    }

    private void checkPriceNullOrNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalPriceException(NULL_OR_NEGATIVE_NUMBER_ERROR_MESSAGE);
        }
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Price price1 = (Price) object;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal multiplyQuantity(long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
