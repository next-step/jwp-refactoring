package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
	boolean existsOrdersByOrderTableIdAndOrderStatusNot(Long orderTableId, OrderStatus orderStatus);
}
