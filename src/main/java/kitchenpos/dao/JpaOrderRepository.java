package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends OrderDao, JpaRepository<Order, Long> {
}
