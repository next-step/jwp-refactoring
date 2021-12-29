package kitchenpos.table.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

}
