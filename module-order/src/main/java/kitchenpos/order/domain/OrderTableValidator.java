package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {

    public OrderTableValidator(){

    }

    public void cancelTableGroup(List<Order> orders) {
        if (isCookingOrEatingOrder(orders)) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING);
        }
    }

    public void checkUpdateTableGroup(List<Order> orders, OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_TABLE_HAS_GROUP);
        }

        if (isCookingOrEatingOrder(orders)) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING);
        }
    }

    public void checkTableEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_STATUS_IS_ALEADY_EMPTY);
        }
    }

    private boolean isCookingOrEatingOrder(List<Order> orders) {
        return orders.stream().anyMatch(order -> order.isCooking() || order.isEating());
    }

}
