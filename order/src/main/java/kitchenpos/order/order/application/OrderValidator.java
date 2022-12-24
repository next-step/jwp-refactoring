package kitchenpos.order.order.application;

public interface OrderValidator {
	void validateUngroup(TableGroup tableGroup);

	void validateChangeEmpty(Long orderTableId);

	void validateCreateOrder(long order);
}
