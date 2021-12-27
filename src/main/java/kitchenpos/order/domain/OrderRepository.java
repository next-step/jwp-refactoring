package kitchenpos.order.domain;

import kitchenpos.common.exception.NotFoundOrderException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    default Order findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundOrderException::new);
    }
}
