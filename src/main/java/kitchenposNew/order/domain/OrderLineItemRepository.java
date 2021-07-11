package kitchenposNew.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    Optional<List<OrderLineItem>> findAllByOrderId(Long orderId);
}
