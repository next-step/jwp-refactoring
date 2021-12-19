package kitchenpos.menu.dto;

import java.math.BigDecimal;

public class MenuRequest {
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }
}
