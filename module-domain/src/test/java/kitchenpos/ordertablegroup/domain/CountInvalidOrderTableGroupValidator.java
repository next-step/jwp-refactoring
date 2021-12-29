package kitchenpos.ordertablegroup.domain;

import java.util.List;

public class CountInvalidOrderTableGroupValidator implements OrderTableGroupValidator {
	@Override
	public void validateOrderTablesAreGreaterThanOrEqualToTwo(List<Long> orderTableIds) {
		throw new IllegalArgumentException();
	}

	@Override
	public void validateNotGrouped(List<Long> orderTableIds) {
		// do not throw exception
	}

	@Override
	public void validateOrderTableIsEmpty(List<Long> orderTableIds) {
		// do not throw exception
	}

	@Override
	public void validateNotCompletedOrderNotExist(Long orderTableGroupId) {
		// do not throw exception
	}
}
