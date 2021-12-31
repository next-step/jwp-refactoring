package kitchenpos.domain.order.infra;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderStatus;
import kitchenpos.domain.order.domain.OrderRepository;
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
