package kitchenpos.order.dao;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    Optional<OrderLineItem> findBySeq(Long seq);

    List<OrderLineItem> findAllByOrder(Order order);
}
