package kitchenpos.table.domain.tablegroup;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
