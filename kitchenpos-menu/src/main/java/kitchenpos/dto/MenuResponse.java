package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;

import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {

    }

    public MenuResponse(Long id, String name, Price price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu, List<MenuProductResponse> menuProducts) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(), menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}