package kitchenpos.common.table.repository;

import java.util.List;
import kitchenpos.common.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByTableGroupId(final Long tableGroupId);
}
