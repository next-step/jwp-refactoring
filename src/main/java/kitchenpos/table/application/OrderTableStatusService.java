package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;

public interface OrderTableStatusService {

    void validateOrderTableStatus(OrderTable orderTable);
}
