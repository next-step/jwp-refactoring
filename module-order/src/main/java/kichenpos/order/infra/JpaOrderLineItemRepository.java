package kichenpos.order.infra;

import kichenpos.order.domain.OrderLineItem;
import kichenpos.order.domain.OrderLineItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderLineItemRepository extends JpaRepository<OrderLineItem, Long>, OrderLineItemRepository {
}
