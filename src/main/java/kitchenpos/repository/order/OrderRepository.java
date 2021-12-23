package kitchenpos.repository.order;

import kitchenpos.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableId(Long orderTableId);
}
