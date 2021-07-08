package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.Order;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order> findOrdersByOrderTableIdIn(List<Long> ids);
}
