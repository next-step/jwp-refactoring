package kitchenpos.table.dto;

public class OrderTableRequest {
	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest() {
	}

	public OrderTableRequest(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
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
		private int numberOfGuests;
		private boolean empty;

		public Builder() {
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

		public OrderTableRequest build() {
			return new OrderTableRequest(id, tableGroupId, numberOfGuests, empty);
		}
	}
}
