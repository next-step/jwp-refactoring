package kitchenpos.ordertable.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    Optional<List<OrderTable>> findAllByIdIn(List<Long> orderTableIds);

    @Query(value = "select ot from OrderTable ot where ot.tableGroupId = :tableGroupId")
    Optional<List<OrderTable>> findListByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
