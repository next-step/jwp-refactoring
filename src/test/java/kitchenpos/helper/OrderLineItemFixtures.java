package kitchenpos.helper;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderLineItemFixtures {

    public static OrderLineItemRequest 주문_항목_요청_만들기(Long orderId, Long menuId, Integer quantity){
        return new OrderLineItemRequest(orderId, menuId, quantity);
    }

    public static OrderLineItemRequest 주문_항목_요청_만들기(Long menuId, Integer quantity){
        return new OrderLineItemRequest(null, menuId, quantity);
    }

    public static OrderLineItem 주문_항목_만들기(Long seq, Order order, Menu menu, Integer quantity){
        return new OrderLineItem(seq, order, menu, quantity);
    }
    public static OrderLineItem 주문_항목_만들기(Order order, Menu menu, Integer quantity){
        return 주문_항목_만들기(null, order, menu, quantity);
    }
    public static OrderLineItem 주문_항목_만들기(Menu menu, Integer quantity){
        return 주문_항목_만들기(null, null, menu, quantity);
    }
}
