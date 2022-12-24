package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.port.OrderPort;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.port.OrderTableValidatorPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static kitchenpos.order.exceptions.OrderErrorCode.GUEST_NOT_NULL_AND_ZERO;
import static kitchenpos.order.exceptions.OrderErrorCode.TABLE_GROUP_NOT_NULL;


@Service
@Transactional(readOnly = true)
public class OrderTableValidator implements OrderTableValidatorPort {

    private final OrderPort orderPort;

    public OrderTableValidator(OrderPort orderPort) {
        this.orderPort = orderPort;
    }

    @Override
    public void validChangeEmpty(OrderTable orderTable) {
        orderPort.findByOrderTableId(orderTable.getId())
                .forEach(Order::validUngroupable);

        validCheckIsNotNullTableGroup(orderTable);
    }

    private void validCheckIsNotNullTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException(TABLE_GROUP_NOT_NULL.getMessage());
        }
    }

    @Override
    public void validChangeNumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < 0 || Objects.isNull(numberOfGuests)) {
            throw new IllegalArgumentException(GUEST_NOT_NULL_AND_ZERO.getMessage());
        }
    }
}
