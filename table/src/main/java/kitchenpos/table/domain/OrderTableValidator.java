package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.exception.NotCreateOrderException;
import kitchenpos.order.exception.OrderErrorCode;
import kitchenpos.table.exception.NotChangeEmptyException;
import kitchenpos.table.exception.NotChangeNumberOfGuestException;
import kitchenpos.table.exception.NotValidOrderException;
import kitchenpos.table.exception.TableErrorCode;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderTableValidator {

    public void validateChangeableEmpty(OrderTable orderTable) {
        for (Order order : orderTable.getOrders()) {
            checkOrderStatus(order);
        }

        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new NotChangeEmptyException(TableErrorCode.ALREADY_ASSIGN_GROUP);
        }
    }

    private void checkOrderStatus(Order order) {
        if (order.isProcessing()) {
            throw new NotValidOrderException();
        }
    }

    public void validateChangeableNumberOfGuests(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new NotChangeNumberOfGuestException(TableErrorCode.EMPTY_TABLE);
        }

        if (orderTable.getNumberOfGuests() < 0) {
            throw new NotChangeNumberOfGuestException(TableErrorCode.GUEST_MORE_THAN_ZERO);
        }
    }

    private void checkEmptyOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new NotCreateOrderException(orderTable.getId() + OrderErrorCode.EMPTY_ORDER_TABLE);
        }
    }
}
