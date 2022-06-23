package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("SELECT ot FROM OrderTable ot WHERE ot.tableGroupId = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(@Param("tableGroupId") Long tableGroupId);

    @Query("SELECT ot FROM OrderTable ot WHERE ot.id IN :orderTableIds")
    List<OrderTable> findAllByIdIn(@Param("orderTableIds") List<Long> orderTableIds);
}
