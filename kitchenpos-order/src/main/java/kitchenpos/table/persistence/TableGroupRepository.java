package kitchenpos.table.persistence;

import kitchenpos.table.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
