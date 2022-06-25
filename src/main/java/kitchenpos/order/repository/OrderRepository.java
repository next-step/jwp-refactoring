package kitchenpos.order.repository;

import kitchenpos.order.domain.OrderV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderV2, Long> {
}
