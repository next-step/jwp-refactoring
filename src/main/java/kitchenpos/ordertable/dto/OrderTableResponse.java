package kitchenpos.ordertable.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {

	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	protected OrderTableResponse() {
	}

	public OrderTableResponse(final Long id, final Long tableGroupId,
		final int numberOfGuests, final boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTableResponse of(final OrderTable orderTable) {
		return Builder.OrderTableResponse()
			.id(orderTable.getId())
			.tableGroupId(orderTable.getTableGroupId())
			.numberOfGuests(orderTable.getNumberOfGuests())
			.empty(orderTable.isEmpty())
			.build();
	}

	public static List<OrderTableResponse> ofList(final List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public static final class Builder {
		private Long id;
		private Long tableGroupId;
		private List<OrderResponse> orderResponses;
		private int numberOfGuests;
		private boolean empty;

		private Builder() {
		}

		public static Builder OrderTableResponse() {
			return new Builder();
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder tableGroupId(Long tableGroupId) {
			this.tableGroupId = tableGroupId;
			return this;
		}

		public Builder numberOfGuests(int numberOfGuests) {
			this.numberOfGuests = numberOfGuests;
			return this;
		}

		public Builder empty(boolean empty) {
			this.empty = empty;
			return this;
		}

		public OrderTableResponse build() {
			return new OrderTableResponse(id, tableGroupId, numberOfGuests, empty);
		}
	}
}
