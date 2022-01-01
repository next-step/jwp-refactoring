package kitchenpos.infra.order;

import kitchenpos.core.domain.Order;
import kitchenpos.core.domain.OrderStatus;
import kitchenpos.core.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return jpaOrderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return jpaOrderRepository.findById(orderId);
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> cookingAndMealStatus) {
        return jpaOrderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, cookingAndMealStatus);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> cookingAndMealStatus) {
        return jpaOrderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, cookingAndMealStatus);
    }
}
