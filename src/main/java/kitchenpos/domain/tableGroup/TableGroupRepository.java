package kitchenpos.domain.tableGroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    boolean existsByOrderTablesOrderTableId(Long orderTableId);
    boolean existsByOrderTablesOrderTableIdIn(List<Long> orderTableIds);
    Optional<TableGroup> findTableGroupByOrderTablesOrderTableId(Long orderTableId);
}
