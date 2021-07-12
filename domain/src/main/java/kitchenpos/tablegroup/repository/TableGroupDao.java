package kitchenpos.tablegroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.tablegroup.domain.TableGroup;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
}
