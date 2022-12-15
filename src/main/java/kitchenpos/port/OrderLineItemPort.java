package kitchenpos.port;

import kitchenpos.domain.OrderLineItem;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemPort {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
