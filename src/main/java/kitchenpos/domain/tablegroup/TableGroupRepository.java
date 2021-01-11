package kitchenpos.domain.tablegroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    @Query(value = "SELECT * FROM table_group AS t " +
            "LEFT JOIN order_table_in_table_group AS o " +
            "ON t.id = o.table_group_id " +
            "where o.order_table_id IN (:order_table_ids)", nativeQuery = true)
    List<TableGroup> findTableGroupsInOrderTableIds(@Param("order_table_ids") List<Long> order_table_ids);

    @Query(value = "SELECT * FROM table_group AS t " +
            "LEFT JOIN order_table_in_table_group AS o " +
            "ON t.id = o.table_group_id " +
            "where o.order_table_id = :order_table", nativeQuery = true)
    Optional<TableGroup> findTableGroupByOrderTableId(@Param("order_table") Long order_table);
}
