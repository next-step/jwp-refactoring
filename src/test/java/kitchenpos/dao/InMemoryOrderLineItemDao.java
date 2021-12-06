package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryOrderLineItemDao extends InMemoryDao<OrderLineItem> implements OrderLineItemDao {

    public InMemoryOrderLineItemDao() {
        super("seq");
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return db.values().stream()
                .filter(orderLineItem -> orderLineItem.getOrderId() == orderId)
                .collect(Collectors.toList());
    }
}
