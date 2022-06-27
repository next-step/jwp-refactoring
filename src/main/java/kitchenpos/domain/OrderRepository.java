package kitchenpos.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList);

    List<Order> findAllByOrderTableId(Long orderTableId);

    Optional<Order> findByOrderTableId(Long orderTableId);
}
