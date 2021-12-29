package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderRepository
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select distinct o from Order o " +
            "left join fetch o.orderLineItems oli")
    List<Order> findAllJoinFetch();

    List<Order> findByOrderTableId(Long id);
}
