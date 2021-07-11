package kitchenpos.order.domain;

import java.util.List;

public interface OrderRepository {
//    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM order o WHERE o.orderTableId = :id AND o.orderStatus IN :orderStatuses")
    boolean existsByOrderTableIdAndOrderStatusIn(Long id, List<OrderStatus> orderStatuses);
}
