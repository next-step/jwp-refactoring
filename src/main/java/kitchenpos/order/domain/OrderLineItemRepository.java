package kitchenpos.order.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItemEntity, Long> {

    List<OrderLineItemEntity> findAllByIdIn(List<Long> orderLineItemIds);
}
