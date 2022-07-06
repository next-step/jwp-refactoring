package kitchenpos.order.domain;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemRepository {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();
}
