package kitchenpos.order.repository;

import kitchenpos.order.domain.OrdersV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrdersV2, Long> {
}
