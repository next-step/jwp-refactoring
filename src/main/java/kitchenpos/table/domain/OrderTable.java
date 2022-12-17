package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.Empty;
import kitchenpos.common.GuestCount;
import kitchenpos.exception.ErrorMessage;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
	private TableGroup tableGroup;
	@Embedded
	private GuestCount guestCount;
	@Embedded
	private Empty empty;

	protected OrderTable() {}

	private OrderTable(int numberOfGuests, boolean empty) {
		this.guestCount = GuestCount.of(numberOfGuests);
		this.empty = Empty.of(empty);
	}

	public static OrderTable of(int numberOfGuests, boolean empty) {
		return new OrderTable(numberOfGuests, empty);
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public GuestCount getGuestCounts() {
		return guestCount;
	}

	public Empty getEmpty() {
		return empty;
	}

	public void updateEmptyStatus(Empty empty) {
		validateTableGroup();
		this.empty = empty;
	}

	public void updateNumberOfGuest(GuestCount guestCounts) {
		validateTableEmpty();
		this.guestCount = guestCounts;
	}

	private void validateTableGroup() {
		if (Objects.nonNull(this.getTableGroup())) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_EMPTINESS_WHEN_TABLE_GROUPED);
		}
	}

	private void validateTableEmpty() {
		if (this.empty.isEmpty()) {
			throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_NUMBER_OF_GUESTS_WHEN_TABLE_IS_EMPTY);
		}
	}

	public Long getTableGroupId() {
		if (Objects.isNull(tableGroup)) {
			return null;
		}
		return tableGroup.getId();
	}

	public void updateGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public boolean isEmpty() {
		return this.empty.isEmpty();
	}

}
