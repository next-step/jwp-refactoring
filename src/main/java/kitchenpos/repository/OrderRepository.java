package kitchenpos.repository;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);

    List<Order> findAllByOrderTableId(Long orderTableId);

    @Query("select distinct o from Order o join fetch o.orderLineItems")
    List<Order> findAllWithItem();

    @Query("select distinct o from Order o join fetch o.orderLineItems where o.id = :id")
    Optional<Order> findByIdWithItem(@Param("id") Long id);
}
