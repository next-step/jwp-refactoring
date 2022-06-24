package kitchenpos.dto.menu;

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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
