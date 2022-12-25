package kitchenpos.order.dao;

import java.util.Optional;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderTableId(Long tableId);
}
