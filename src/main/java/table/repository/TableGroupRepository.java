package table.repository;

import org.springframework.data.jpa.repository.*;

import table.domain.*;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
