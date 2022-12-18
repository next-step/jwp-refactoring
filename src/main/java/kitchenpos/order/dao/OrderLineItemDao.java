package kitchenpos.order.dao;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
