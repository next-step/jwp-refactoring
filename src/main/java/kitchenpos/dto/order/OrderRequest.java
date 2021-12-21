package kitchenpos.dto.order;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;

public class OrderRequest {

    private Long orderTableId;

    private OrderLineItemRequests orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = new OrderLineItemRequests(orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderLineItemRequests getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.getMenuIds();
    }

    public Order toOrder(OrderTable orderTable, List<Menu> menus) {
        return new Order(orderTable, orderLineItems.toOrderLineItems(menus));
    }
}
