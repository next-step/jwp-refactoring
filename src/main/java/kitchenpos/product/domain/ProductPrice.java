package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {
    @Column(name = "price")
    int value;

    protected ProductPrice() {
    }

    private ProductPrice(int price) {
        validatePrice(price);
        this.value = price;
    }

    public static ProductPrice from(int price) {
        return new ProductPrice(price);
    }

    private void validatePrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getValue() {
        return value;
    }
}
