package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.orderTableId = :orderTableId")
    List<Order> findAllByOrderTableId(Long orderTableId);

    @Query("SELECT o FROM Order o WHERE o.orderTableId IN :orderTableIds")
    List<Order> findAllByOrderTableIds(List<Long> orderTableIds);
}
