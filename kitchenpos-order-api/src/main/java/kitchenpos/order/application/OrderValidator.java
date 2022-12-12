package kitchenpos.order.application;

import kitchenpos.tablegroup.domain.OrderTable;

import java.util.List;

public interface OrderValidator {
    void validateToCreateOrder(Long orderTableId, List<Long> menuIds);
    void validateToChangeEmpty(OrderTable orderTable);
    void validateToUngroup(List<Long> orderTableIds);
}
