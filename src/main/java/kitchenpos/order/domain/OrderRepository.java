package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderTableId(Long orderTableId);

    List<Order> findByOrderTableIdIn(List<Long> orderTableIds);
}
