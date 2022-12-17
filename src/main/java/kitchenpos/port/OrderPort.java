package kitchenpos.port;

import kitchenpos.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderPort {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findAllByOrderTableIdIn(List<Long> orderTablesId);

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
