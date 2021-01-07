package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.exception.AlreadyTableGroupException;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.NegativeNumberException;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "table_group_id")
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	protected OrderTable() {
		this.numberOfGuests = 0;
		this.empty = true;
	}

	public static OrderTable create() {
		return new OrderTable();
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

	public void changeNumberOfGuests(final int numberOfGuests) {
		validationBeforeChangeNumberOfGuests(numberOfGuests);
		this.numberOfGuests = numberOfGuests;
	}

	public void changeEmpty(final boolean empty) {
		validationBeforeChangeEmpty();
		this.empty = empty;
	}

	private void validationBeforeChangeNumberOfGuests(final int numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new NegativeNumberException("손님 수는 음수를 지정할 수 없습니다.");
		}

		if (this.empty) {
			throw new EmptyTableException("손님 수는 빈 테이블 상태일 때는 지정할 수 없습니다.");
		}

	}

	private void validationBeforeChangeEmpty() {
		if (this.tableGroupId != null) {
			throw new AlreadyTableGroupException("단체 테이블이 지정되어 있어 상태를 변경할 수 없습니다.");
		}
	}

	public void saveGroupInfo(Long tableGroupId) {
		changeEmpty(false);
		this.tableGroupId = tableGroupId;
	}

	public void ungroup() {
		this.tableGroupId = null;
	}
}
