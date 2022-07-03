package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductRequests {
    List<MenuProductRequest> elements;

    public MenuProductRequests(List<MenuProductRequest> elements) {
        this.elements = elements;
    }

    public List<MenuProduct> toMenuProducts() {
        return elements.stream()
                .map(MenuProductRequest::of)
                .collect(Collectors.toList());
    }
}
