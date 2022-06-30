package kitchenpos.table.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    @Query("select ot.id from OrderTable ot where ot.tableGroupId = :tableGroupId")
    List<Long> findIdsByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
