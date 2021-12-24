package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProducts;

public class MenuProductResponse {

    private String name;
    private Long quantity;

    public static List<MenuProductResponse> ofList(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts().stream()
            .map(menuProduct -> of(menuProduct.getProduct().getName(), menuProduct.getQuantity()))
            .collect(Collectors.toList());
    }

    public static MenuProductResponse of(String name, Long quantity) {
        return new MenuProductResponse(name, quantity);
    }

    private MenuProductResponse(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public MenuProductResponse() {
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }
}
