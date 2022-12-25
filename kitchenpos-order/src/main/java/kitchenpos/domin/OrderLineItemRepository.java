package kitchenpos.domin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findOrderLineItemsByOrderId(Long orderId);
}
