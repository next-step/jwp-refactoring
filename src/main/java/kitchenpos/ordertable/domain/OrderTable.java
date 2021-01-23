package kitchenpos.ordertable.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Guest numberOfGuests;

	private boolean empty;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	protected OrderTable() {
	}

	private OrderTable(final Long id, final int numberOfGuests, final boolean empty) {
		this.id = id;
		this.numberOfGuests = Guest.of(numberOfGuests);
		this.empty = empty;
	}

	public static OrderTable of(final Long id, final int numberOfGuests, final boolean empty) {
		return new OrderTable(id, numberOfGuests, empty);
	}

	public static OrderTable of(final int numberOfGuests, final boolean empty) {
		return of(null, numberOfGuests, empty);
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		validateIsEmpty();
		this.numberOfGuests = Guest.of(numberOfGuests);
	}

	private void validateIsEmpty() {
		if (this.isEmpty()) {
			throw new IllegalArgumentException("테이블이 비어있습니다.");
		}
	}

	public int numberOfGuests() {
		return this.numberOfGuests.getNumberOfGuests();
	}

	public Long getId() {
		return id;
	}

	public Guest getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public void changeEmptyState(boolean empty) {
		validateIsGroup();
		this.empty = empty;
	}

	private void validateIsGroup() {
		if (Objects.nonNull(this.getTableGroup())) {
			throw new IllegalArgumentException("단체 테이블은 변경할 수 없습니다.");
		}
	}
}
