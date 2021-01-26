package kitchenpos.dto.request;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public static MenuRequest of(Menu menu) {
        List<MenuProductRequest> requests = menu.getMenuProducts().stream()
                .map(MenuProductRequest::of)
                .collect(Collectors.toList());

        return new MenuRequest(menu.getName(), menu.getPrice().getMoney(), menu.getMenuGroup().getId(), requests);
    }

    public MenuRequest(String name, BigDecimal price, long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toMenu(MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
