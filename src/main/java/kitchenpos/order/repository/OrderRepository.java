package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.OrderStatusV2;
import kitchenpos.order.domain.OrdersV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrdersV2, Long> {
    boolean existsOrdersV2ByOrderTableIdAndOrderStatusNot(Long orderTableId, OrderStatusV2 orderStatus);
    boolean existsOrdersV2ByOrderTableIdInAndOrderStatusNot(List<Long> orderTableIds, OrderStatusV2 orderStatus);
}
