package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {

}
