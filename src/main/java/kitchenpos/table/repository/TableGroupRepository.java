package kitchenpos.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.table.domain.TableGroup;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
}
