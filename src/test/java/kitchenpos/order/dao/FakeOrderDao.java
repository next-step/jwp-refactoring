package kitchenpos.order.dao;

import static kitchenpos.ServiceTestFactory.createOrderBy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class FakeOrderDao implements OrderDao {
    public static Order COMPLETE_ORDER = createOrderBy(1L, 1L, OrderStatus.COMPLETION.name());
    public static Order COOKING_ORDER = createOrderBy(2L, 2L, OrderStatus.COOKING.name());
    public static Order MEAL_ORDER = createOrderBy(2L, 3L, OrderStatus.MEAL.name());

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
        Order find = Arrays.asList(COMPLETE_ORDER, COOKING_ORDER, MEAL_ORDER).stream()
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
