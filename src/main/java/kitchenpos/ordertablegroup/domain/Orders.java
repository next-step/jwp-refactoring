package kitchenpos.ordertablegroup.domain;

import java.util.List;

public interface Orders {
	List<Order> findByOrderTableId(Long orderTableId);
}
