package kitchenpos.order.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findByOrder(Order order);
}
