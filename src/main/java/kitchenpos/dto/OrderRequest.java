package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Quantity;
import kitchenpos.domain.order.OrderLineMenu;
import kitchenpos.domain.order.Orders;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineMenuRequest> orderLineMenuRequests;

    public OrderRequest() {}

    public OrderRequest(Long orderTableId, List<OrderLineMenuRequest> orderLineMenuRequests) {
        this.orderTableId = orderTableId;
        this.orderLineMenuRequests = orderLineMenuRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineMenuRequest> getOrderLineMenuRequests() {
        return orderLineMenuRequests;
    }

    public List<Long> getOrderIds() {
        return orderLineMenuRequests.stream().map(OrderLineMenuRequest::getMenuId).collect(toList());
    }

    public void putOrderMenu(Orders order, Map<Long, Menu> menus) {
        orderLineMenuRequests.forEach(m -> order.addOrderLineMenu(new OrderLineMenu(order, menus.get(m.getMenuId()), new Quantity(m.getQuantity()))));
    }
}
