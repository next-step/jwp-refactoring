package kitchenpos.dao;

import kitchenpos.domain.OrderTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeOrderTableDao implements OrderTableDao {
    private Map<Long, OrderTable> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public OrderTable save(OrderTable orderTable) {
        orderTable.createId(key);
        map.put(key, orderTable);
        key++;
        return orderTable;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return null;
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return null;
    }
}
