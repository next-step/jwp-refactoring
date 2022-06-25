package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    boolean existsOrdersV2ByOrderTableIdAndOrderStatusNot(Long orderTableId, OrderStatus orderStatus);
    boolean existsOrdersV2ByOrderTableIdInAndOrderStatusNot(List<Long> orderTableIds, OrderStatus orderStatus);
}
