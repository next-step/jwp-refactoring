package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
