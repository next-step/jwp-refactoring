package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select o from Order o "
        + "join fetch o.orderLineItems")
    List<Order> findAllOrderAndItems();

    @Query(value = "select o from Order o "
        + "join fetch o.orderLineItems oli "
        + "where o = :order")
    Order findAllOrderAndItemsByOrder(Order order);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
