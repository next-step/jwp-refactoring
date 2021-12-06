package kitchenpos.dao;

import kitchenpos.domain.OrderTable;

import java.util.List;

public class InMemoryOrderTableDao extends InMemoryDao<OrderTable> implements OrderTableDao {

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return null;
    }
}
