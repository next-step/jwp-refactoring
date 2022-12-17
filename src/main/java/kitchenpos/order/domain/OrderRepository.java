package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderTable.id = :orderTableId")
    List<Order> findAllByOrderTableId(@Param("orderTableId") Long orderTableId);

    @Query("SELECT o FROM Order o WHERE o.orderTable.id IN :orderTableIds")
    List<Order> findAllByOrderTableIds(@Param("orderTableIds") List<Long> orderTableIds);
}
