package kitchenpos.tablegroup.infrastructure.respository;

import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupJpaRepository extends JpaRepository<TableGroup, Long> {
}
