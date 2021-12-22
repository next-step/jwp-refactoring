package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select distinct o from Order o "
        + "join fetch o.orderTable ot "
        + "join fetch o.orderLineItems.orderLineItems ol")
    List<Order> findOrders();
}
