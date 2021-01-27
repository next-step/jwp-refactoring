package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private static final int MIN_PRICE = 0;

    private int price;

    protected Price() {}

    public Price(int price) {
        checkGreaterThanZero(price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void checkGreaterThanZero(int price) {
        if (price < MIN_PRICE) {
            throw new IllegalArgumentException("마이너스 금액을 가질 수 없습니다");
        }
    }

    public int getAmount(int quantity) {
        return price * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price1 = (Price) o;
        return price == price1.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
