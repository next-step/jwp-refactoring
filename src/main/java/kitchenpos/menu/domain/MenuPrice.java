package kitchenpos.menu.domain;

import kitchenpos.exception.IllegalPriceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
public class MenuPrice {
    @Column(name = "price")
    private int value;

    protected MenuPrice() {
    }

    private MenuPrice(int value) {
        validatePrice(value);
        this.value = value;
    }

    public static MenuPrice from(int price) {
        return new MenuPrice(price);
    }

    private static void validatePrice(int value) {
        if (value < 0) {
            throw new IllegalPriceException("가격은 %d 미만일 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isLargerThan(int value) {
        return this.value > value;
    }
}
