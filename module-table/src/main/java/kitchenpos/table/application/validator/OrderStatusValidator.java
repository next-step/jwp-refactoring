package kitchenpos.table.application.validator;

import java.util.List;

public interface OrderStatusValidator {
    void existsByOrderTableIdAndOrderStatusIn(Long orderTableId);
    void existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds);
}
