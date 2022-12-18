package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select distinct o " +
            "from Order o " +
            "left join fetch o.orderLineItems")
    List<Order> findAllWithLineItems();

    @Query("select  o " +
            "from Order o " +
            "left join fetch o.orderLineItems " +
            "where o.id = :id"
    )
    Optional<Order> findWithLineItemById(Long id);
}
