package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
//    private MenuGroupResponse menuGroup;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts = new ArrayList<>();

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), toMenuProductResponse(menu.getMenuProducts()));
    }

    private static MenuGroupResponse toMenuGroupResponse(MenuGroup menuGroup) {
        return MenuGroupResponse.from(menuGroup);
    }

    private static List<MenuProductResponse> toMenuProductResponse(List<MenuProduct> menuProducts) {
        return MenuProductResponse.from(menuProducts);
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

//    public MenuGroupResponse getMenuGroup() {
//        return menuGroup;
//    }


    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
