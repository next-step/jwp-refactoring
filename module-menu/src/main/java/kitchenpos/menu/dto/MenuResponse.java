package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {
    private Long id;
    private String name;
    private Long price;
    private MenuGroupResponse menuGroup;
    private List<MenuProduct> menuProducts;

    public MenuResponse(Long id, String name, Price price, MenuGroupResponse menuGroup,
                        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price.value();
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
