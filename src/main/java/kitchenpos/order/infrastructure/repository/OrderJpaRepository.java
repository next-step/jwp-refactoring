package kitchenpos.order.infrastructure.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableId);

    List<Order> findByOrderTableId(Long orderTableId);
}
