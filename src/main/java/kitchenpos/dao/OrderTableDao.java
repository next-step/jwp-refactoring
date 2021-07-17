package kitchenpos.dao;

import kitchenpos.domain.OrderTableRequest;

import java.util.List;
import java.util.Optional;

public interface OrderTableDao {
    OrderTableRequest save(OrderTableRequest entity);

    Optional<OrderTableRequest> findById(Long id);

    List<OrderTableRequest> findAll();

    List<OrderTableRequest> findAllByIdIn(List<Long> ids);

    List<OrderTableRequest> findAllByTableGroupId(Long tableGroupId);
}
