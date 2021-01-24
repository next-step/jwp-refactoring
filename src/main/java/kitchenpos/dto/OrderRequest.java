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
    private String orderStatus;
    private List<OrderLineMenuRequest> orderLineMenuRequests;

    public OrderRequest() {}

    public OrderRequest(Long orderTableId, String orderStatus, List<OrderLineMenuRequest> orderLineMenuRequests) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineMenuRequests = orderLineMenuRequests;
    }

    public OrderRequest(Long orderTableId, List<OrderLineMenuRequest> orderLineMenuRequests) {
        this(orderTableId, "COOKING", orderLineMenuRequests);
    }

    public OrderRequest(String orderStatus) {
        this(null, orderStatus, null);
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void putOrderMenu(Orders order, Map<Long, Menu> menus) {
        orderLineMenuRequests.forEach(m -> order.addOrderLineMenu(new OrderLineMenu(order, menus.get(m.getMenuId()), new Quantity(m.getQuantity()))));
    }
}
