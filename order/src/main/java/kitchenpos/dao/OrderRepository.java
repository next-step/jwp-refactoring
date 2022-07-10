package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderTableId(Long id);
    @Query("SELECT o.id FROM OrderTable o WHERE o.id = :id")
    Long existsOrderTableById(Long id);
    @Query("SELECT o.empty FROM OrderTable o WHERE o.id = :id")
    Boolean isEmptyTable(Long id);
}
