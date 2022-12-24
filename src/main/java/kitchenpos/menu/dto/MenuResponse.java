package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.dto.MenuProductResponse;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private java.lang.Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {

    }

    public MenuResponse(Long id, String name, BigDecimal price, java.lang.Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu, List<MenuProductResponse> menuProducts) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(), menuProducts);
    }

    public java.lang.Long getId() {
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}