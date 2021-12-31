package kitchenpos.application.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private final String name;
    private final int price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProductRequests = new ArrayList<>();

    public MenuRequest(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests.addAll(menuProducts);
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProductRequests.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList());
    }

}
