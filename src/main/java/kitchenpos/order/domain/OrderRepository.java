package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

	List<Orders> findAllByOrderTable(OrderTable orderTable);
}
