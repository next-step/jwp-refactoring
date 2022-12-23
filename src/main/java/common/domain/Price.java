package common.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<BigDecimal> {

    private static final String INVALID_PRICE = "유요하지 않은 가격입니다.";

    private BigDecimal price;

    protected Price() {

    }

    public Price(int price) {
        BigDecimal target = new BigDecimal(price);
        validatePriceValue(target);
        this.price = target;
    }

    private void validatePriceValue(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(INVALID_PRICE);
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(BigDecimal o) {
        return price.compareTo(o);
    }
}
