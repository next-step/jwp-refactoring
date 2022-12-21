package kitchenpos.order.dao;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableId(Long orderTableId);
    boolean existsByOrderTableIdInAndOrderStatusNot(List<Long> orderTableIds, OrderStatus orderStatus);
}
