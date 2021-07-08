package kitchenpos.menu.dto;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private Price price;
    private MenuGroup menuGroup;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse(Long id) {
        this.id = id;
    }

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup(), menu.getMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public void setPrice(BigDecimal price) {
        this.price = new Price(price);
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
