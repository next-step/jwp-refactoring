package kitchenpos.ordertable.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {
    @Query("select o from OrderTable o where o.tableGroup.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
