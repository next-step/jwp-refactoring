package kitchenpos.order.domain;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
	COOKING, MEAL, COMPLETION;

	@JsonCreator
	public static OrderStatus create(@JsonProperty("orderStatus") String requestString) {
		return Stream.of(values())
			.filter(it -> it.name().equals(requestString.toUpperCase()))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
}
