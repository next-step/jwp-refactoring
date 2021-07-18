package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

import static kitchenpos.common.Constants.MINIMUM_NUMBER_OF_GUEST;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	protected OrderTable() {
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this(numberOfGuests, empty);
		this.id = id;
		this.tableGroupId = tableGroupId;
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public void addTableGroupId(final Long tableGroupId) {
		doesExistTableGroupId();
		this.tableGroupId = tableGroupId;
	}

	public void doesExistTableGroupId() {
		if (tableGroupId != null) {
			throw new IllegalArgumentException("이미 테이블 그룹이 할당되어 있습니다.");
		}
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void changeNumberOfGuests(final int numberOfGuests) {
		validateChangeNumberOfGuests(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	private void validateChangeNumberOfGuests(int numberOfGuests) {
		if (empty) {
			throw new IllegalArgumentException("주문 테이블이 빈 테이블입니다.");
		}

		if (numberOfGuests < MINIMUM_NUMBER_OF_GUEST) {
			throw new IllegalArgumentException("손님 숫자는 0보다 작을 수 없습니다.");
		}
	}

	public boolean isEmpty() {
		return empty;
	}

	public void removeTableGroupId() {
		tableGroupId = null;
	}

	public void fillTable() {
		if (!this.empty) {
			throw new IllegalArgumentException("주문 테이블이 비어있지 않습니다.");
		}
		this.empty = false;
	}

	public void changeEmpty(boolean empty) {
		doesExistTableGroupId();
		this.empty = empty;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderTable that = (OrderTable) o;
		return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tableGroupId, numberOfGuests, empty);
	}
}
