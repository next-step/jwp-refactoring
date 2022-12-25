package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.orderTable.id = :orderTableId")
    List<Order> findAllByOrderTableId(Long orderTableId);

    @Query("SELECT o FROM Order o WHERE o.orderTable.id IN :orderTableIds")
    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatus);

    boolean existsByOrderTableIdInAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatus);
}
