package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;

import java.util.List;

public class OrderTableValidator {

    private List<Order> orders;
    private OrderTable orderTable;

    public OrderTableValidator(List<Order> orders, OrderTable orderTable) {
        this(orderTable);
        this.orders = orders;
    }

    public OrderTableValidator(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void cancelTableGroup() {
        if (isCookingOrEatingOrder()) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING);
        }
    }

    public void checkUpdateTableGroup() {
        if (orderTable.hasTableGroup()) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_TABLE_HAS_GROUP);
        }

        if (isCookingOrEatingOrder()) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_IS_COOKING_OR_IS_EATING);
        }
    }

    public void checkTableEmpty() {
        if (orderTable.isEmpty()) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_STATUS_IS_ALEADY_EMPTY);
        }
    }

    private boolean isCookingOrEatingOrder() {
        return this.orders.stream().anyMatch(order -> order.isCooking() || order.isEating());
    }

}
