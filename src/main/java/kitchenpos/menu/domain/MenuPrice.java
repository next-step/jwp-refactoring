package kitchenpos.menu.domain;

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
            throw new IllegalArgumentException();
        }
    }

    public int getValue() {
        return value;
    }

    public boolean isLargerThan(int value) {
        return this.value > value;
    }
}
