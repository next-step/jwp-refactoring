package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.order.domain.OrderLineItem;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
