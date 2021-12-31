package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    public void isEmptyOrderLineItem(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getOrderLineItems())) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_LINE_IS_EMPTY);
        }
    }

    public void isAlreadyCompletionOrder(Order order) {
        if (OrderStatus.COMPLETION.name().equals(order.getOrderStatus())) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER);
        }
    }
}
