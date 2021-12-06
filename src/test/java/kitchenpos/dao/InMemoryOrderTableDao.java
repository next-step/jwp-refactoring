package kitchenpos.dao;

import kitchenpos.domain.OrderTable;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryOrderTableDao extends InMemoryDao<OrderTable> implements OrderTableDao {

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return db.values().stream()
                .filter(orderTable -> ids.contains(orderTable.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return null;
    }
}
