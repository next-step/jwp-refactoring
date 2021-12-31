package kitchenpos.dto.menu;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.Menu;


public class MenuRequest {

    private String name;

    private Integer price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, Integer price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return Menu.of(name, price, menuGroupId, menuProducts.stream()
            .map(MenuProductRequest::toEntity)
            .collect(toList()));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
