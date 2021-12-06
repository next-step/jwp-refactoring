package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;

import java.util.List;

public class InMemoryOrderLineItemDao extends InMemoryDao<OrderLineItem> implements OrderLineItemDao {

    public InMemoryOrderLineItemDao() {
        super("seq");
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return null;
    }
}
