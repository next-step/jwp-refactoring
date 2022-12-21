package kitchenpos.order.port;

import kitchenpos.order.domain.OrderTable;

import java.util.List;

public interface OrderTablePort {
    OrderTable save(OrderTable entity);

    OrderTable findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long id);
}
