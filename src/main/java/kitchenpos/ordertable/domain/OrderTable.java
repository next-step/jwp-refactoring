package kitchenpos.ordertable.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "order_table")
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTable() {
	}

	public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public OrderTable(boolean empty) {
		this.empty = empty;
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		validateEmpty(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	public void changeEmpty(boolean empty) {
		validateNumberOfGuest();
		this.empty = empty;
	}

	public void setTableGroup(Long tableGroupId) {
		validateTableGroup(tableGroupId);
		this.tableGroupId = tableGroupId;
		this.empty = false;
	}

	public void unTableGroup() {
		this.tableGroupId = null;
	}

	private void validateTableGroup(Long tableGroupId) {
		if (tableGroupId == null) {
			throw new IllegalArgumentException("테이블그룹 ID가 없습니다.");
		}

		if (this.tableGroupId != null) {
			throw new IllegalArgumentException("단체 지정이 불가능한 테이블입니다.");
		}
	}

	private void validateEmpty(int numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new IllegalArgumentException("게스트 수는 0명 이상이어야 합니다.");
		}

		if (this.empty) {
			throw new IllegalArgumentException("테이블이 비어있습니다.");
		}
	}

	private void validateNumberOfGuest() {
		if (this.tableGroupId != null) {
			throw new IllegalArgumentException("단체 지정된 테이블은 상태를 변경할 수 없습니다.");
		}
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
}
