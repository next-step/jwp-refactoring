package kitchenpos.product.ui.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Menu;

public final class MenuResponse {

    private long id;
    private String name;
    private BigDecimal price;
    private long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    private MenuResponse(long id, String name, BigDecimal price, long menuGroupId,
        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
            menu.id(),
            menu.name().value(),
            menu.price().value(),
            menu.menuGroup().id(),
            MenuProductResponse.listFrom(menu.menuProducts())
        );
    }

    public static List<MenuResponse> listFrom(List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
