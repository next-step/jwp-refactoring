package kitchenpos.dao;

import kitchenpos.domain.OrderRequest;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    OrderRequest save(OrderRequest entity);

    Optional<OrderRequest> findById(Long id);

    List<OrderRequest> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);
}
