package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;

public class MenuResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProduct> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        final Price price = menu.getPrice();
        return new MenuResponse(menu.getId(), menu.getName(), price.value(),
            menu.getMenuGroupId(), menu.getMenuProducts());
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
