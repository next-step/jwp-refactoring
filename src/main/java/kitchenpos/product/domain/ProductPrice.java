package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {
    @Column(name = "price")
    long value;

    public static final int MIN_PRICE = 0;

    protected ProductPrice() {
    }

    private ProductPrice(long price) {
        validatePrice(price);
        this.value = price;
    }

    public static ProductPrice from(long price) {
        return new ProductPrice(price);
    }

    private void validatePrice(long price) {
        if (price < MIN_PRICE) {
            throw new IllegalArgumentException();
        }
    }

    public long getValue() {
        return value;
    }
}
