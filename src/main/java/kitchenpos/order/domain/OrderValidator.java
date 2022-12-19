package kitchenpos.order.domain;

public interface OrderValidator {
	void unGroupOrderStatusValidate(Long orderTableId);

	void changeEmptyOrderStatusValidate(Long orderTableId);

	void orderTableExistValidate(Long orderTableId);

	void orderTableEmptyValidate(Long orderTableId);
}
