package kitchenpos.domin;

import java.util.List;

public class OrderLineItems {

    private static final String NO_ORDER_LINE_ITEM_EXCEPTION = "주문 항목이 존재하지 않습니다.";

    List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItemList(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItemList(List<OrderLineItem> orderLineItemList) {
        if (orderLineItemList.isEmpty()) {
            throw new IllegalArgumentException(NO_ORDER_LINE_ITEM_EXCEPTION);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

}
