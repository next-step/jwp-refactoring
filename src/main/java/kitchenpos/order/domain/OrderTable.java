package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
	private TableGroup tableGroup;

	@Embedded
	private NumberOfGuests numberOfGuests;

	@Embedded
	private TableEmpty tableEmpty;

	protected OrderTable() {
	}

	private OrderTable(NumberOfGuests numberOfGuests, TableEmpty tableEmpty) {
		Assert.notNull(numberOfGuests, "인원수는 필수입니다.");
		Assert.notNull(tableEmpty, "테이블 상태는 필수입니다.");
		this.numberOfGuests = numberOfGuests;
		this.tableEmpty = tableEmpty;
	}

	public static OrderTable of(NumberOfGuests numberOfGuests, TableEmpty tableEmpty) {
		return new OrderTable(numberOfGuests, tableEmpty);
	}

	public Long id() {
		return id;
	}

	public TableGroup tableGroup() {
		return tableGroup;
	}

	public boolean hasTableGroup() {
		return tableGroup != null;
	}

	public int getNumberOfGuests() {
		return numberOfGuests.value();
	}

	public boolean isEmpty() {
		return tableEmpty.isEmpty();
	}

	public void updateEmpty(boolean empty) {
		if (hasTableGroup()) {
			throw new IllegalArgumentException("테이블 그룹에 속한 테이블은 상태를 변경할 수 없습니다.");
		}
		this.tableEmpty = TableEmpty.from(empty);
	}


	public NumberOfGuests numberOfGuests() {
		return numberOfGuests;
	}

	public void updateNumberOfGuests(int numberOfGuests) {
		validateNumberOfGuests();
		this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
	}

	private void validateNumberOfGuests() {
		if (isEmpty()) {
			throw new IllegalArgumentException("빈 테이블은 인원수를 변경할 수 없습니다.");
		}
	}

	public boolean isFull() {
		return tableEmpty.isFull();
	}

	public void updateGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public boolean emptyAndNoGroup() {
		return isEmpty() && !hasTableGroup();
	}


	public boolean isOrdered() {
		return false;
	}
}
