package kitchenpos.port;

import kitchenpos.domain.OrderTable;

import java.util.List;
import java.util.Optional;

public interface OrderTablePort {
    OrderTable save(OrderTable entity);

    OrderTable findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
