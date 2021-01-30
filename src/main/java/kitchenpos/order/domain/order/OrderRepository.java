package kitchenpos.order.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
