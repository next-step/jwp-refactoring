package kitchenpos.ordertable.domain;

public class ValidOrderTableValidator implements OrderTableValidator {
	@Override
	public void validateNotCompletedOrderNotExist(Long id) {
		// do not throw exception
	}
}
