package kitchenpos.order.repository;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> status);
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> status);
    @Query("select o from Order o join fetch o.orderLineItems")
    List<Order> findAllJoinFetch();
}
