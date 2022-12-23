package kitchenpos.table.port;

import kitchenpos.table.domain.OrderTable;

import java.util.List;

public interface OrderTablePort {
    OrderTable save(OrderTable entity);

    OrderTable findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long id);

    List<OrderTable> findAllByTableGroupIdIn(List<Long> tableGroupIds);
}
