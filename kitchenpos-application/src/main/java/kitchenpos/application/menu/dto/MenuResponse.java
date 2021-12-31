package kitchenpos.application.menu.dto;

import kitchenpos.core.menu.domain.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final List<MenuProductResponse> menuProducts = new ArrayList<>();
    private final Long menuGroupId;


    public MenuResponse(Long id, String name, int price, List<MenuProductResponse> menuProducts, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts.addAll(menuProducts);
        this.menuGroupId = menuGroupId;
    }

    public static MenuResponse of(Menu menu) {
        final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().intValue(), menuProductResponses, menu.getMenuGroupId());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        MenuResponse that = (MenuResponse) target;

        if (price != that.price) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!menuProducts.equals(that.menuProducts)) return false;
        return Objects.equals(menuGroupId, that.menuGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuProducts, menuGroupId);
    }
}
