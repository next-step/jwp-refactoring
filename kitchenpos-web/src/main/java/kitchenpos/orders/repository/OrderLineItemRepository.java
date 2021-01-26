package kitchenpos.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.orders.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
