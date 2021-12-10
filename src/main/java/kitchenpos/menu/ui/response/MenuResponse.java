package kitchenpos.menu.ui.response;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;

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
        return null;
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
