package kitchenpos.ordertable.domain;

public interface OrderTableValidator {
	void validateNotCompletedOrderNotExist(Long id);
}
