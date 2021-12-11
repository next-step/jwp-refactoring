package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductPrice {

    private static final int MIN_PRICE = 0;

    @Column
    private BigDecimal price;

    protected ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        check(price);
        this.price = price;
    }

    public ProductPrice(int price) {
        this(new BigDecimal(price));
    }

    private void check(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MIN_PRICE) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal multiply(long number) {
        return price.multiply(new BigDecimal(number));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
