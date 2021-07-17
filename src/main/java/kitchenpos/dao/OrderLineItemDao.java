package kitchenpos.dao;

import kitchenpos.domain.OrderLineItemRequest;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemDao {
    OrderLineItemRequest save(OrderLineItemRequest entity);

    Optional<OrderLineItemRequest> findById(Long id);

    List<OrderLineItemRequest> findAll();

    List<OrderLineItemRequest> findAllByOrderId(Long orderId);
}
