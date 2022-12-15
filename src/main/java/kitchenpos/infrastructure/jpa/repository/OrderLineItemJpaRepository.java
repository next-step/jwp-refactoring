package kitchenpos.infrastructure.jpa.repository;

import kitchenpos.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineItemJpaRepository extends JpaRepository<OrderLineItem, Long> {
}
