package kitchenpos.tableGroup.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.tableGroup.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
