package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<Price> {

    @Column
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price는 0이상이어야 합니다.");
        }
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(Price o) {
        return price.compareTo(o.getPrice());
    }

    public boolean isMoreExpensive(Price totalPrice) {
        return price.compareTo(totalPrice.getPrice()) > 0;
    }
}
