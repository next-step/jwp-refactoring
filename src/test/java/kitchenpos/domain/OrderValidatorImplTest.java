package kitchenpos.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderValidator;
import kitchenpos.table.exception.CannotChangeEmptyException;

import java.util.Arrays;
import java.util.List;

public class OrderValidatorImplTest implements OrderValidator {

    public static final long ID_FOR_EXCEPTION = 1L;

    public OrderValidatorImplTest() {
    }

    public void canUngroupOrChange(Long id) {
        if (id == ID_FOR_EXCEPTION) {
            throw new CannotChangeEmptyException();
        }
    }

    public void canUngroupOrChangeOrderList(List<Long> orderTableIds) {
        if (orderTableIds.contains(ID_FOR_EXCEPTION)) {
            throw new CannotChangeEmptyException();
        }
    }
}
