package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;

public interface OrderTableService {

    void validateOrderTableStatus(OrderTable orderTable);
}
