package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price of(BigDecimal price) {
        validatePrice(price);
        return new Price(price);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원보다 커야합니다");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void validateTotalPrice(BigDecimal totalPrice) {
        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
    }
}