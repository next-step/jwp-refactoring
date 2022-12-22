package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> asList);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> tableIds, List<OrderStatus> orderStatuses);

}
