package kitchenpos.table.repository;

import kitchenpos.table.domain.TableGroupV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroupV2, Long> {
}
