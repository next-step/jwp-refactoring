package kitchenpos.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(Collection<Long> orderTableIds, Collection<OrderStatus> orderStatus);
}
