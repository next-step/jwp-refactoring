package kitchenpos.dto;

import java.util.List;

import kitchenpos.domain.Menu;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
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
}
