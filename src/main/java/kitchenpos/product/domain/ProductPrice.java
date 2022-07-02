package kitchenpos.product.domain;

import kitchenpos.exception.IllegalPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {
    @Column(name = "price")
    int value;

    public static final String ERROR_PRICE_TOO_SMALL = "가격은 %d 미만일 수 없습니다.";
    public static final int MIN_PRICE = 0;

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
        if (price < MIN_PRICE) {
            throw new IllegalPriceException(String.format(ERROR_PRICE_TOO_SMALL, MIN_PRICE));
        }
    }

    public int getValue() {
        return value;
    }
}
