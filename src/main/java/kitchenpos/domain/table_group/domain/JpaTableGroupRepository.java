package kitchenpos.domain.table_group.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends TableGroupRepository, JpaRepository<TableGroup, Long> {
}
