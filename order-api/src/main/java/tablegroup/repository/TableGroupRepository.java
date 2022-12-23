package tablegroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tablegroup.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
