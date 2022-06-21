package kitchenpos.table.dao;

import static kitchenpos.ServiceTestFactory.EMPTY_ORDER_TABLE;
import static kitchenpos.ServiceTestFactory.ORDER_TABLE;
import static kitchenpos.ServiceTestFactory.OTHER_ORDER_TABLE;
import static kitchenpos.ServiceTestFactory.THIRD_ORDER_TABLE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class FakeOrderTableDao implements OrderTableDao {
    @Override
    public OrderTable save(OrderTable entity) {
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Arrays.asList(ORDER_TABLE, OTHER_ORDER_TABLE, THIRD_ORDER_TABLE, EMPTY_ORDER_TABLE).stream()
                .filter(orderTable -> orderTable.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<OrderTable> findAll() {
        return Collections.singletonList(ORDER_TABLE);
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
