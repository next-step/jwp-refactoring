package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuProductResponses {
    private List<MenuProductResponse> menuProductResponses;

    protected MenuProductResponses() {
    }

    public MenuProductResponses(List<MenuProductResponse> menuProductResponses) {
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuProductResponses of(List<MenuProduct> menuProducts) {
        return new MenuProductResponses(menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList()));
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponses that = (MenuProductResponses) o;
        return Objects.equals(menuProductResponses, that.menuProductResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProductResponses);
    }
}
