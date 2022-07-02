package kitchenpos.domain.tablegroup;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    @Override
    @EntityGraph(attributePaths = { "orderTables" })
    Optional<TableGroup> findById(Long id);
}
