package kitchenpos.dto.order;

import java.security.InvalidParameterException;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toOrderLineItem(List<Menu> menus) {
        Menu menu = menus.stream()
            .filter(targetMenu -> targetMenu.isSame(menuId))
            .findFirst()
            .orElseThrow(() -> new InvalidParameterException("일치하는 메뉴가 없습니다."));
        return OrderLineItem.of(menu, quantity);
    }
}
