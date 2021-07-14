package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderTableId(Long orderTableId);

    @Query(value = "select o from Order o join fetch o.orderTable t join t.tableGroup tg where tg.id = :tableGroupId")
    List<Order> findAllByTableGroupId(@Param("tableGroupId") Long tableGroupId);
}
