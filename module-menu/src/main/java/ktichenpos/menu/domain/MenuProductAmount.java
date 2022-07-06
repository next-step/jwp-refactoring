package ktichenpos.menu.domain;

import java.math.BigDecimal;

public class MenuProductAmount {

    private final long quantity;
    private final BigDecimal price;

    public MenuProductAmount(long quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
    }

    BigDecimal getAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
