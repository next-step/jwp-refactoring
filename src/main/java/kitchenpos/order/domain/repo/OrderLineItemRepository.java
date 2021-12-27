package kitchenpos.order.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.order.domain.domain.OrderLineItem;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
