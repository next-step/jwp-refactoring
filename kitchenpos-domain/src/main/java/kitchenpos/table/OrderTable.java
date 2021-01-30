package kitchenpos.table;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long tableGroupId;
	private NumberOfGuests numberOfGuests;
	private boolean empty;

	protected OrderTable() {
	}

	private OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
		this.empty = empty;
	}

	public static OrderTableBuilder builder() {
		return new OrderTableBuilder();
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public void setTableGroupId(Long tableGroupId) {
		this.tableGroupId = tableGroupId;
	}

	public boolean isGroupTable() {
		return Objects.nonNull(tableGroupId);
	}

	public int getNumberOfGuests() {
		return numberOfGuests.getNumberOfGuests();
	}

	public void changeGuestsNumber(int guestsNumber) {
		this.numberOfGuests = new NumberOfGuests(guestsNumber);
	}

	public boolean isEmpty() {
		return empty;
	}

	public void changeEmpty(boolean empty) {
		this.empty = empty;
	}

	public static final class OrderTableBuilder {
		private Long tableGroupId;
		private int numberOfGuests;
		private boolean empty;

		private OrderTableBuilder() {
		}

		public OrderTableBuilder tableGroupId(Long tableGroupId) {
			this.tableGroupId = tableGroupId;
			return this;
		}

		public OrderTableBuilder numberOfGuests(int numberOfGuests) {
			this.numberOfGuests = numberOfGuests;
			return this;
		}

		public OrderTableBuilder empty(boolean empty) {
			this.empty = empty;
			return this;
		}

		public OrderTable build() {
			return new OrderTable(tableGroupId, numberOfGuests, empty);
		}
	}
}
