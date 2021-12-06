package kitchenpos.dao;

import kitchenpos.domain.Order;

import java.util.List;

public class InMemoryOrderDao extends InMemoryDao<Order> implements OrderDao {

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return false;
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return false;
    }
}
