package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.port.OrderLineItemPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderLineItemJpaAdapter implements OrderLineItemPort {
    @Override
    public OrderLineItem save(OrderLineItem entity) {
        return null;
    }

    @Override
    public Optional<OrderLineItem> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderLineItem> findAll() {
        return null;
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return null;
    }
}
