package kitchenpos.web.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.orders.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
