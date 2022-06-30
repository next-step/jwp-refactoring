package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, BigDecimal price, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductResponse> list = menu.getMenuProducts().getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getPrice().getValue(), menu.getMenuGroup().getId(), list);
    }

    public Long getId() {
        return id;
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
