package kitchenpos.table.application;

import java.util.List;

public interface TableStatusService {
    void validateOrderTableStatus(Long orderTableId);

    void validateOrderTableStatus(List<Long> orderTableIds);
}
