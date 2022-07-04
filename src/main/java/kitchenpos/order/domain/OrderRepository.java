package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select o from Order o "
            + "join fetch o.orderLineItems")
    List<Order> findAllOrderAndItems();

    @Query(value = "select o from Order o "
            + "join fetch o.orderLineItems oli "
            + "where o = :order")
    Order findAllOrderAndItemsByOrder(Order order);

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTableEntities, List<OrderStatus> orderStatuses);
}
