package kitchenpos.infra.tablegroup;

import kitchenpos.core.tablegroup.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {
}
