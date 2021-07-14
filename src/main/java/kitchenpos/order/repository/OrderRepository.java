package kitchenpos.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
