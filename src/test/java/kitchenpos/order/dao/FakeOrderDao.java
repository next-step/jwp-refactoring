package kitchenpos.order.dao;

import static kitchenpos.ServiceTestFactory.COMPLETE_ORDER;
import static kitchenpos.ServiceTestFactory.COOKING_ORDER;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;

public class FakeOrderDao implements OrderDao {
    @Override
    public Order save(Order entity) {
        return null;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        Order find = Arrays.asList(COMPLETE_ORDER, COOKING_ORDER).stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId))
                .findFirst()
                .orElse(COMPLETE_ORDER);
        return orderStatuses.contains(find.getOrderStatus());
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return false;
    }
}
