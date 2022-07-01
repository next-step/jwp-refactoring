package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemRepository extends JpaRepository<OrderLineItem, Long>, OrderLineItemRepository {
}
