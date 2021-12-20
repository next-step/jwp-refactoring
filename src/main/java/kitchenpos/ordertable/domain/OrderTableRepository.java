package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository {
	OrderTable save(OrderTable orderTable);

	List<OrderTable> findAll();

	Optional<OrderTable> findById(Long id);
}
