package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
	boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

	boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

	@Query(value = "select distinct o from Order o" +
		" join fetch o.orderTable m" +
		" left join fetch o.orderLineItems.orderLineItems oi" +
		" left join fetch oi.menu me" +
		" left join fetch me.menuGroup mg"
	)
	List<Order> findAllFetch();
}
