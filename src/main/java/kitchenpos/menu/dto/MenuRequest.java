package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

import java.math.BigDecimal;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroup menuGroup;

    public MenuRequest() {
    }

    public MenuRequest(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public Menu toMenu() {
        return new Menu(name, price, menuGroup);
    }
}
