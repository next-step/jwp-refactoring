package kitchenpos.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Quantity;

public class OrderLineItemRequest {
    private Long menuId;
    private Quantity quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public MenuQuantityPair toMenuQuantityPair(List<Menu> menus) {
        return new MenuQuantityPair(findById(menus), quantity);
    }

    private Menu findById(List<Menu> menus) {
        return menus.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }
}
