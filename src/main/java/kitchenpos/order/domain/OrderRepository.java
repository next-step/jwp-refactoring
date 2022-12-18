package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);
    List<Order> findAllByOrderTableId(Long orderTableId);

}
