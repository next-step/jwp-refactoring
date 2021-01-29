package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    public MenuRequest() {
    }

    public MenuRequest(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Long getId() {
        return id;
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

    public Menu toMenu() {
        return new Menu(name, price, menuGroupId);
    }
}
