package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderTableId(Long tableId);

    List<Order> findAllByOrderTableIdIn(List<Long> orderTablesIds);
}
