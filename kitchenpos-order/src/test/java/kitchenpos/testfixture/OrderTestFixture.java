package kitchenpos.testfixture;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.domain.*;
import kitchenpos.table.domain.*;

import java.math.BigDecimal;
import java.util.List;

public class OrderTestFixture {
    public static MenuProduct createMenuProduct(Long productId, int quantity) {
        return new MenuProduct(productId, (long) quantity);
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static Order createOrder(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderLineItems);
    }

    public static OrderRequest createOrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
    }

    public static OrderMenu createOrderMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        return new OrderMenu(menuId, menuName, menuPrice);
    }

    public static OrderLineItemRequest createOrderLineItemRequest(Long menuId, String menuName, BigDecimal price, int quantity) {
        return new OrderLineItemRequest(menuId, menuName, price, (long) quantity);
    }

    public static OrderLineItem createOrderLineItem(OrderMenu orderMenu, long quantity) {
        return new OrderLineItem(orderMenu, quantity);
    }

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }
}
