package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public interface OrderDao extends JpaRepository<Order, Long> {
    boolean existsByOrderTable_IdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);
    boolean existsByOrderTable_IdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
