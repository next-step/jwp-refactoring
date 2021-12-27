package kitchenpos.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.order.domain.OrderTable;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    @Query("select ot from OrderTable ot "
        + "join fetch ot.tableGroup tg")
    List<OrderTable> findOrderTables();

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
}
