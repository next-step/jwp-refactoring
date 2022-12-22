package kitchenpos.table.application.validator;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;

public interface OrderStatusValidator {
    void existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList);
}
