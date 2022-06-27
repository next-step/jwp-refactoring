package kitchenpos.tableGroup.domain;

import kitchenpos.orderTable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
