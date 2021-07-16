package kitchenpos.table.application;

import java.util.Optional;

import kitchenpos.table.domain.OrderTable;

public interface TableOrderService {
    Optional<OrderTable> findTableById(Long orderTableId);
}
