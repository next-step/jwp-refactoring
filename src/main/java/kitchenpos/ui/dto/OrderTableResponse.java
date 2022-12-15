package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable2;

public class OrderTableResponse {

	private Long id;
	private Integer numberOfGuests;
	private Boolean empty;

	private OrderTableResponse() {
	}

	public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTableResponse(OrderTable2 orderTable) {
		this(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
	}

	public static List<OrderTableResponse> of(List<OrderTable2> orderTables) {
		return orderTables.stream().map(OrderTableResponse::new).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean getEmpty() {
		return empty;
	}
}
