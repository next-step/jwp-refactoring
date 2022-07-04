package kitchenpos.tableGroup.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    @EntityGraph(attributePaths = {"orderTables"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<TableGroup> findWithOrderTablesById(Long tableGroupId);
}
