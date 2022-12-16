package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByOrderTableId(Long orderTableId);
}
