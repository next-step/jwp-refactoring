package kitchenpos.table.repository;

import kitchenpos.table.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    @Query("select distinct t from TableGroup t join fetch t.orderTables.orderTables where t.id = :id")
    Optional<TableGroup> findByIdWithOrderTable(@Param("id") Long id);
}
