package table_group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import table_group.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
