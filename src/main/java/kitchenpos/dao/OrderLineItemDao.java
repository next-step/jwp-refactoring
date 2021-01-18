package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrderId(Long orderId);
}
