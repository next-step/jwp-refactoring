package kitchenpos.common.menu.dto;

import java.util.List;
import kitchenpos.common.menu.domain.Menu;

public class MenuResponse {
    private Long id;
    private String name;
    private Long price;

    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, Long price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(
                menu.getId(), menu.getName(), menu.getPrice().value(), menu.getMenuGroupId(), menu.getMenuProducts());
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
