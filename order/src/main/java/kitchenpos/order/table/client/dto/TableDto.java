package kitchenpos.order.table.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TableDto {

	private final boolean empty;

	@JsonCreator
	public TableDto(@JsonProperty("empty") boolean empty) {
		this.empty = empty;
	}

	public boolean isEmpty() {
		return empty;
	}
}
