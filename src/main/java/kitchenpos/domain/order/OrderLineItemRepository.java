package kitchenpos.domain.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

}
