package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.port.OrderPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static kitchenpos.constants.ErrorCodeType.GUEST_NOT_NULL_AND_ZERO;
import static kitchenpos.constants.ErrorCodeType.TABLE_GROUP_NOT_NULL;

@Service
@Transactional
public class OrderTableValidator {

    private final OrderPort orderPort;

    public OrderTableValidator(OrderPort orderPort) {
        this.orderPort = orderPort;
    }

    public void validChangeEmpty(OrderTable orderTable) {
        orderPort.findByOrderTableId(orderTable.getId())
                .forEach(Order::validUngroupable);

        validCheckIsNotNullTableGroup(orderTable);
    }

    private void validCheckIsNotNullTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException(TABLE_GROUP_NOT_NULL.getMessage());
        }
    }

    public void validChangeNumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < 0 || Objects.isNull(numberOfGuests)) {
            throw new IllegalArgumentException(GUEST_NOT_NULL_AND_ZERO.getMessage());
        }
    }
}
