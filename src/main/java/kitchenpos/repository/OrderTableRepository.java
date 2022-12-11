package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    @Query(value = "SELECT ot FROM OrderTable ot WHERE ot.tableGroupId =:tableGroupId")
    List<OrderTable> findAllByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
