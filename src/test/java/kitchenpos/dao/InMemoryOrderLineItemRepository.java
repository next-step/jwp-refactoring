package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryOrderLineItemRepository extends InMemoryRepository<OrderLineItem> implements OrderLineItemRepository {

    public InMemoryOrderLineItemRepository() {
        super("seq");
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return db.values().stream()
                .filter(orderLineItem -> orderLineItem.getOrder().getId() == orderId)
                .collect(Collectors.toList());
    }
}
