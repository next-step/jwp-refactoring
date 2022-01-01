package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private String name;

    @NotNull(message = "가격은 빈 값이 들어올 수 없습니다.")
    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProduct> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, new BigDecimal(price), menuGroupId, menuProducts);
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Menu toMenu(MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup, menuProducts);
    }
}
