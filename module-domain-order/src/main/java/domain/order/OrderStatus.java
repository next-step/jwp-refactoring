package domain.order;

public enum OrderStatus {
	COOKING, MEAL, COMPLETION;

	public boolean isOngoing() {
		return this == COOKING || this == MEAL;
	}
}
