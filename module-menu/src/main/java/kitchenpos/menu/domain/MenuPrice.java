package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {
    @Column(name = "price")
    private int value;

    public static final String ERROR_PRICE_TOO_SMALL = "가격은 %d 미만일 수 없습니다.";
    public static final int MIN_PRICE = 0;

    protected MenuPrice() {
    }

    private MenuPrice(int value) {
        validatePrice(value);
        this.value = value;
    }

    public static MenuPrice from(int price) {
        return new MenuPrice(price);
    }

    private void validatePrice(int value) {
        if (value < MIN_PRICE) {
            throw new IllegalPriceException(String.format(ERROR_PRICE_TOO_SMALL, MIN_PRICE));
        }
    }

    public boolean isLargerThan(int value) {
        return this.value > value;
    }

    public int getValue() {
        return value;
    }
}
