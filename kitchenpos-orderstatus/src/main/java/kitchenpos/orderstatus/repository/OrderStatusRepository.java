package kitchenpos.orderstatus.repository;

import kitchenpos.orderstatus.domain.OrderStatus;
import kitchenpos.orderstatus.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    OrderStatus findByOrderId(long orderId);

    boolean existsByOrderTableIdAndStatusIn(long orderTableId, List<Status> statuses);

    boolean existsByOrderTableIdInAndStatusIn(List<Long> ordertableIds, List<Status> statuses);
}
