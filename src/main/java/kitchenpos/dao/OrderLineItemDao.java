package kitchenpos.dao;

import kitchenpos.domain.order.OrderLineItem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findAllByOrderId(Long orderId);
}
