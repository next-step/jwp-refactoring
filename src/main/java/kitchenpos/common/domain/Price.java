package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private static final int FREE = 0;

    @Column(nullable = false)
    private int price;

    protected Price() {
    }

    public Price(Integer price) {
        validate(price);
        this.price = price;
    }

    public boolean overTo(Price otherPrice) {
        return price > otherPrice.getPrice();
    }

    private void validate(Integer price) {
        if (price == null || price < FREE) {
            throw new IllegalArgumentException("[ERROR] 가격은 0원 이상 이어야 합니다.");
        }
    }

    public int getPrice() {
        return price;
    }
}
