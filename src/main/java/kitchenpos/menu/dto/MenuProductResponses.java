package kitchenpos.menu.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponses {
    private List<MenuProductResponse> menuProductResponses;

    private MenuProductResponses(List<MenuProductResponse> menuProductResponses) {
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuProductResponses from(List<MenuProduct> menuProducts) {
        return new MenuProductResponses(menuProducts.stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList()));
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return Collections.unmodifiableList(menuProductResponses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MenuProductResponses))
            return false;
        MenuProductResponses that = (MenuProductResponses)o;
        return Objects.equals(menuProductResponses, that.menuProductResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProductResponses);
    }
}
