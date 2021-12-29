package kitchenpos.ordertable.domain;

public class NotCompletedOrderExistOrderTableValidator implements OrderTableValidator {
	@Override
	public void validateNotCompletedOrderNotExist(Long id) {
		throw new IllegalStateException();
	}
}
