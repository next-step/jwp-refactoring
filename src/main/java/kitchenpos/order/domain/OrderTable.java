package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private TableGroup tableGroup;
	private NumberOfGuests numberOfGuests;
	private boolean empty;

	protected OrderTable() {
	}

	private OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
		this.tableGroup = tableGroup;
		this.numberOfGuests = new NumberOfGuests(numberOfGuests);
		this.empty = empty;
	}

	public static OrderTableBuilder builder() {
		return new OrderTableBuilder();
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public boolean isGroupTable() {
		return Objects.nonNull(tableGroup);
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
		private TableGroup tableGroup;
		private int numberOfGuests;
		private boolean empty;

		private OrderTableBuilder() {
		}

		public OrderTableBuilder tableGroup(TableGroup tableGroup) {
			this.tableGroup = tableGroup;
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
			return new OrderTable(tableGroup, numberOfGuests, empty);
		}
	}
}
