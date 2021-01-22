package kitchenpos.order.domain;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderTableIdEquals(long orderTableId);
}
