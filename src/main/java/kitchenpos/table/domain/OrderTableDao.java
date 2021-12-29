package kitchenpos.table.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    @Query("SELECT orderTable FROM OrderTable orderTable WHERE orderTable.tableGroup.id = :tableGroupId")
    List<OrderTable> findByTableGroupId(Long tableGroupId);

}
