package kitchenpos.domain.tablegroup.infra;

import kitchenpos.domain.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {
}
