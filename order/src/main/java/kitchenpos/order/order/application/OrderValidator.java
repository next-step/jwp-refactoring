package kitchenpos.order.order.application;

import kitchenpos.table.domain.TableGroup;

public interface OrderValidator {
	void validateUngroup(TableGroup tableGroup);

	void validateChangeEmpty(Long orderTableId);

	void validateCreateOrder(long order);
}
