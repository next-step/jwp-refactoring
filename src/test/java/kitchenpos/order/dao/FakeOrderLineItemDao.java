package kitchenpos.order.dao;

import static kitchenpos.ServiceTestFactory.createOrderLineItemBy;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.infrastructure.OrderLineItemDao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FakeOrderLineItemDao implements OrderLineItemDao {
    private final OrderLineItem orderLineItem = createOrderLineItemBy(1L, 1L, 1L, 1);
    private List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

    @Override
    public OrderLineItem save(OrderLineItem entity) {
        return entity;
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderLineItem> findAll() {
        return orderLineItems;
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return orderLineItems;
    }
}
