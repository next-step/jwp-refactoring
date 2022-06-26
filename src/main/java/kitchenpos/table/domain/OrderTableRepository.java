package kitchenpos.table.domain;

import java.util.List;
import kitchenpos.tableGroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
