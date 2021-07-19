package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByOrderTableIdIn(List<Long> orderTableIds);

    Optional<Orders> findByOrderTableId(Long orderTableId);
}
