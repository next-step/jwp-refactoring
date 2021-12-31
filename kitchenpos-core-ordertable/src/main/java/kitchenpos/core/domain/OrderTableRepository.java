package kitchenpos.core.domain;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository {
    OrderTable save(OrderTable orderTable);

    List<OrderTable> findAll();

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllById(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    List<OrderTable> saveAll(List<OrderTable> orderTables);
}
