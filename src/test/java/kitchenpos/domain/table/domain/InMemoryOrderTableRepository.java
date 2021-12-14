package kitchenpos.domain.table.domain;

import kitchenpos.common.InMemoryRepository;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryOrderTableRepository extends InMemoryRepository<OrderTable> implements OrderTableRepository {

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return db.values().stream()
                .filter(orderTable -> ids.contains(orderTable.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return db.values().stream()
                .filter(orderTable -> orderTable.getTableGroup().getId() == tableGroupId)
                .collect(Collectors.toList());
    }
}
