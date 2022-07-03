package kitchenpos.table.domain;

import kitchenpos.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    boolean existsByOrderTableIdAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatusList);

    List<OrderTable> findAllByTableGroupIds(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
