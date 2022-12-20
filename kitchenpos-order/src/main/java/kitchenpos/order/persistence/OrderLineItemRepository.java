package kitchenpos.order.persistence;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrder(Order order);
}
