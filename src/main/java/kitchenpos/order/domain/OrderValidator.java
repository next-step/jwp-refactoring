package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import org.springframework.util.CollectionUtils;

public class OrderValidator {

    private Order order;
    private OrderLineItems orderLineItems;

    public OrderValidator(Order order) {
        this.order = order;
    }

    public OrderValidator(OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void isEmptyOrderLineItem() {
        if (CollectionUtils.isEmpty(orderLineItems.getOrderLineItems())) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_LINE_IS_EMPTY);
        }
    }

    public void isAlreadyCompletionOrder(){
        if (OrderStatus.COMPLETION.name().equals(this.order.getOrderStatus())) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER);
        }
    }

}
