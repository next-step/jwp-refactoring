package kitchenpos.order.repository;

import kitchenpos.order.domain.OrderLineItemV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItemV2, Long> {
}
