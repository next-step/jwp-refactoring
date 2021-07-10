package kitchenpos.dao;

import kitchenpos.domain.order.OrderTableGroup;

import java.util.List;
import java.util.Optional;

public interface TableGroupDao {
    OrderTableGroup save(OrderTableGroup entity);

    Optional<OrderTableGroup> findById(Long id);

    List<OrderTableGroup> findAll();
}
