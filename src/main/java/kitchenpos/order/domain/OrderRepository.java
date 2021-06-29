package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableAndOrderStatusIn(Long orderTableId, Iterable<String> status);

    boolean existsByOrderTableInAndOrderStatusIn(Iterable<Long> orderTableIds, Iterable<String> status);
}
