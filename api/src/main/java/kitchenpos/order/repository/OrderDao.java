package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.Order;

public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findOrdersByOrderTableIdIn(List<Long> ids);
}
