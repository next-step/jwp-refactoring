package kitchenpos.domain.repository;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderTableId(final Long orderTableId);
}
