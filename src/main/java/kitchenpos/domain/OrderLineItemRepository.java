package kitchenpos.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<Order, Long> {
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
