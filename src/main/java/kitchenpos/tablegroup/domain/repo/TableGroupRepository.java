package kitchenpos.tablegroup.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.tablegroup.domain.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
