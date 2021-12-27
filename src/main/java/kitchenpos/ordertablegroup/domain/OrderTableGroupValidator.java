package kitchenpos.ordertablegroup.domain;

import java.util.List;

public interface OrderTableGroupValidator {
	void validateOrderTablesAreGreaterThanOrEqualToTwo(List<Long> orderTableIds);

	void validateNotGrouped(List<Long> orderTableIds);

	void validateOrderTableIsEmpty(List<Long> orderTableIds);

	void validateNotCompletedOrderNotExist(Long orderTableGroupId);
}
