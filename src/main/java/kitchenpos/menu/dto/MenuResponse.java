package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProductResponses;

    protected MenuResponse() {}

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
        MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductResponses = MenuProductResponse.ofMenuProducts(menuProducts);
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName().value(), menu.getPrice().value(),
            menu.getMenuGroup().getId(), menu.getMenuProducts());
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
