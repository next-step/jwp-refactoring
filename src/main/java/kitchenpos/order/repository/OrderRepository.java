package kitchenpos.order.repository;

import java.util.List;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o join fetch o.orderLineItems.orderLineItems")
    List<Order> findAllWithOrderLineItems();

    @Query(value = "select o from Order o where o.orderTableId = :orderTableId")
    List<Order> findByOrderTableId(@Param("orderTableId") Long orderTableId);
}
