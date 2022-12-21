package kitchenpos.order.port;

import kitchenpos.order.domain.Order;

import java.util.List;

public interface OrderPort {
    Order save(Order entity);

    Order findById(Long id);

    List<Order> findAll();

    List<Order> findAllByOrderTableIdIn(List<Long> orderTablesId);

    List<Order> findByOrderTableId(Long orderTableID);
}
