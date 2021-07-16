package kitchenpos.application.table;

import java.util.List;

public interface OrderValidator {
    void checkNotCompletionOrder(Long orderTableId);
    void checkNotCompletionOrders(List<Long> orderTableIds);
}
