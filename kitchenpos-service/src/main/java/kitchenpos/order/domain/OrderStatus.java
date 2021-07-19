package kitchenpos.order.domain;

import java.util.Objects;

public enum OrderStatus {
	COOKING, MEAL, COMPLETION;

	public boolean isCompletion() {
		return Objects.equals(OrderStatus.COMPLETION, this);
	}
}
