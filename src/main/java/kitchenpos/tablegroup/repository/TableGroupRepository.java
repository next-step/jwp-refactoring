package kitchenpos.tablegroup.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

}
