package kitchenpos.order.dao;

import static kitchenpos.ServiceTestFactory.createOrderBy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class FakeOrderDao implements OrderDao {
    private final Order completeOrder = createOrderBy(1L, 1L, OrderStatus.COMPLETION.name());
    private final Order secondCompleteOrder = createOrderBy(2L, 2L, OrderStatus.COMPLETION.name());
    private final Order mealOrder = createOrderBy(3L, 3L, OrderStatus.MEAL.name());
    private final Order cookingOrder = createOrderBy(4L, 4L, OrderStatus.COOKING.name());
    private final Order cookingSaveOrder = createOrderBy(1L, 1L, OrderStatus.COOKING.name());
    private final Order completeSaveOrder = createOrderBy(2L, 2L, OrderStatus.COMPLETION.name());

    @Override
    public Order save(Order entity) {
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Arrays.asList(cookingSaveOrder, completeSaveOrder)
                .stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Order> findAll() {
        return Arrays.asList(cookingSaveOrder);
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        Order find = Arrays.asList(completeOrder, secondCompleteOrder, cookingOrder, mealOrder).stream()
                .filter(order -> order.getOrderTableId().equals(orderTableId))
                .findFirst()
                .orElse(completeOrder);
        return orderStatuses.contains(find.getOrderStatus());
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        boolean result = false;
        for (Long id : orderTableIds) {
            result = existsByOrderTableIdAndOrderStatusIn(id, orderStatuses);
        }
        return result;
    }
}
