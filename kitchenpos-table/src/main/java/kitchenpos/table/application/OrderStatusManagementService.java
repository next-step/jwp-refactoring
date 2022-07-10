package kitchenpos.table.application;

import java.util.List;

public interface OrderStatusManagementService {
    void validateOrderStatusBeChanged(Long orderTableId);

    void validateOrderStatusBeChangedByIds(List<Long> orderTableIds);
}
