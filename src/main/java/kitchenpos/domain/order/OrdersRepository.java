package kitchenpos.domain.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    boolean existsByOrderTableIdAndOrderStatus(Long orderTableId, OrderStatus orderStatuses);
    
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableId(Long orderTableId);
}
