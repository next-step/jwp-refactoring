package order.repository;

import org.springframework.data.jpa.repository.*;

import order.domain.*;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
