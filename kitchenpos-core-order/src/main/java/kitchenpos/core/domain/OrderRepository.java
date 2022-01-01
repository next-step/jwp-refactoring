package kitchenpos.core.domain;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    List<Order> findAll();

    Optional<Order> findById(Long orderId);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> cookingAndMealStatus);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> cookingAndMealStatus);
}
