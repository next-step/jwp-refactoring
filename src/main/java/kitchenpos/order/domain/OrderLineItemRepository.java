package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    @Query("SELECT oli FROM OrderLineItem oli WHERE oli.order.id = :orderId")
    List<OrderLineItem> findAllByOrderId(@Param("orderId") Long orderId);

}
