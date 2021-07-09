package kitchenpos.domain;

import java.util.Arrays;

public enum OrderStatus {
	COOKING, MEAL, COMPLETION;

	public static boolean isUnChangeable(OrderStatus status) {
		return Arrays.asList(COOKING, MEAL).contains(status);
	}

	public static boolean isComplete(OrderStatus orderStatus) {
		return COMPLETION == orderStatus;
	}
}
