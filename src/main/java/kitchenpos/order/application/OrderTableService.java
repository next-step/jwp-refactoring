package kitchenpos.order.application;

import kitchenpos.table.domain.OrderTable;

public interface OrderTableService {

    void validateOrderTableStatus(OrderTable orderTable);
}
