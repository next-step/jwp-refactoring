package kitchenpos.table.domain;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);
}
