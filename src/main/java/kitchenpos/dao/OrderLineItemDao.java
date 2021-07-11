package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.OrderLineItem;

public interface OrderLineItemDao extends JpaRepository<OrderLineItem, Long> {
    OrderLineItem save(OrderLineItem entity);

    Optional<OrderLineItem> findBySeq(Long id);

    List<OrderLineItem> findAll();

    List<OrderLineItem> findAllByOrder_Id(Long orderId);
}
