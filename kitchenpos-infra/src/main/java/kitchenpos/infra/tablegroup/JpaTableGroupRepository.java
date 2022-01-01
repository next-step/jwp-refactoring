package kitchenpos.infra.tablegroup;

import kitchenpos.core.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long> {
}
