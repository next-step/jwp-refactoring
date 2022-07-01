package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemRepository {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findById(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrder(Order order);
}
