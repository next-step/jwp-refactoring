package kitchenpos.order.domain;

import kitchenpos.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    long countByOrderTableIdInAndOrderStatus(List<Long> orderTableId, OrderStatus orderStatus);
}
