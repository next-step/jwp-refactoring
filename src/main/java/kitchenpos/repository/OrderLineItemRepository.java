package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.OrderLineItem;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
}
