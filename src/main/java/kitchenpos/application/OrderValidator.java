package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

@Component
public class OrderValidator {
    public void validateSave(Order order, OrderTable orderTable, long menuCount) {
        validateOrderLineItemsEmpty(order.getOrderLineItems());
        validateOrderLineItemsSize(order.getOrderLineItems(), menuCount);
        validateOrderTableIsEmpty(orderTable);
    }

    private void validateOrderLineItemsEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private void validateOrderLineItemsSize(List<OrderLineItem> orderLineItems, long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException("주문 항목에 등록되어 있지 않은 메뉴가 존재합니다.");
        }
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }
}
