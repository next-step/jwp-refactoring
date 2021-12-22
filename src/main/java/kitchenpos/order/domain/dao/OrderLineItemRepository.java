package kitchenpos.order.domain.dao;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long>,
    OrderLineItemDao {

    @Override
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
