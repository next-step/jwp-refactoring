package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {
    private static final int FREE = 0;

    @Column(nullable = false)
    private int price;

    protected MenuPrice() {
    }

    public MenuPrice(Integer price) {
        validate(price);
        this.price = price;
    }

    private void validate(Integer price) {
        if (price == null || price < FREE) {
            throw new IllegalArgumentException("[ERROR] 메뉴 가격은 0원 이상 이어야 합니다.");
        }
    }

    public boolean overTo(Amounts amounts) {
        return price > amounts.calculateTotalAmount();
    }

    public int getPrice() {
        return price;
    }

    public boolean overTo(MenuProducts menuProducts) {
        return price > menuProducts.getTotalPrice();
    }
}
