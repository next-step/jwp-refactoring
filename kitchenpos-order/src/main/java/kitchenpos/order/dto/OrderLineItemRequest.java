package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {}

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemRequest> toResponselist(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getOrderMenu().getMenuId(),
                        orderLineItem.getQuantity()))
                .collect(toList());
    }

    public OrderLineItem toOrderLineItem(Menu menu) {
        OrderMenu orderMenu = new OrderMenu.Builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .menuPrice(menu.getPrice())
                .build();

        return new OrderLineItem(orderMenu, this.quantity);
    }

    public OrderLineItem toOrderLineItem(Order order, List<Menu> menus) {
        OrderMenu target = menus.stream()
                .filter(menu -> menu.getId().equals(menuId))
                .findFirst()
                .map(it -> new OrderMenu(it.getId(), it.getName(), it.getPrice()))
                .orElseThrow(IllegalArgumentException::new);

        return new OrderLineItem(order, target, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
