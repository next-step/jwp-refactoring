package kitchenpos.order.domain;

public enum OrderStatus {
	COOKING, MEAL, COMPLETION;

	public boolean isOngoing() {
		return this == COOKING || this == MEAL;
	}
}
