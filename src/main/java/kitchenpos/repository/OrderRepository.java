package kitchenpos.repository;

import kitchenpos.domain.OrderEntity;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByOrderTableAndOrderStatusIn(OrderTableEntity orderTable, List<OrderStatus> orderStatuses);
}
