package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.NoSuchElementException;

public interface OrderRepository extends JpaRepository<Order, Long> {
    default Order getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("주문을 찾을 수 없습니다. id: " + id));
    }
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);
}
