package kitchenpos.ordertablegroup.domain;

import java.util.Optional;

public interface OrderTableGroupRepository {
	OrderTableGroup save(OrderTableGroup orderTableGroup);

	Optional<OrderTableGroup> findById(Long id);
}
