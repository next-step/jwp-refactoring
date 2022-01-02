package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.*;

import kitchenpos.order.domain.*;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
