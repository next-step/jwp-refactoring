package kichenpos.order.product.model;

import java.math.BigDecimal;

public final class Menu {

    private Long id;
    private BigDecimal price;
    private String name;

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
