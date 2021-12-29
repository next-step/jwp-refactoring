package kitchenpos.table.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.table.domain.TableGroup;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    @Query("select distinct t from TableGroup t join fetch t.orderTables")
    Optional<TableGroup> findByIdWithOrderTable(Long tableGroupId);

}
