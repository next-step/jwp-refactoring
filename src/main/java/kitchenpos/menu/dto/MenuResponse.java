package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuEntity;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    protected MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static MenuResponse of(MenuEntity menu) {
        return new MenuResponse(menu.getId()
                , menu.getName()
                , menu.getPrice()
                , menu.getMenuGroupId()
                , MenuProductResponse.ofList(menu.getMenuProductsValues()));
    }
}
