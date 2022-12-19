package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPriceValue(), menu.getMenuGroupId()
                , getMenuProductResponses(menu));
    }

    public static List<MenuResponse> getMenuResponses(Menus menus) {
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private static List<MenuProductResponse> getMenuProductResponses(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProductResponse> menuProducts) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuResponse that = (MenuResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && (price.compareTo(that.price) == 0) && Objects.equals(menuGroupId, that.menuGroupId)
                && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
