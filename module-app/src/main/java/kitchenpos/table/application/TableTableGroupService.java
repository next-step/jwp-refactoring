package kitchenpos.table.application;

import java.util.List;

import kitchenpos.table.domain.OrderTable;

public interface TableTableGroupService {
    List<OrderTable> findOrderTableByIds(List<Long> orderTableIds);

    List<Long> findOrderTableIdsByTableGroupId(Long tableGroupId);
}
