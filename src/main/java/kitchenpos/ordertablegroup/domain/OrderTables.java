package kitchenpos.ordertablegroup.domain;

import java.util.List;

public interface OrderTables {
	List<OrderTable> findAllByIdIn(List<Long> ids);

	List<OrderTable> findByOrderTableGroupId(Long orderTableGroupId);
}
