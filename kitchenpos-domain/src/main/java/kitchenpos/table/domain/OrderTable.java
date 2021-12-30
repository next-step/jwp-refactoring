package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Entity
@Table(name = "order_table")
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
	@ManyToOne(fetch = FetchType.LAZY)
	private TableGroup tableGroup;

	@Embedded
	@Column(nullable = false)
	private NumberOfGuests numberOfGuests;

	@Column(nullable = false)
	private boolean empty;

	protected OrderTable() {
	}

	private OrderTable(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static OrderTable of(Long id, TableGroup tableGroup, NumberOfGuests numberOfGuests, boolean empty) {
		return new OrderTable(id, tableGroup, numberOfGuests, empty);
	}

	public static OrderTable of(NumberOfGuests numberOfGuests, boolean empty) {
		return of(null, null, numberOfGuests, empty);
	}

	public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
		return new OrderTable(id, null, NumberOfGuests.of(numberOfGuests), empty);
	}

	public void changeEmptyStatus(boolean empty) {
		if (Objects.nonNull(tableGroup)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "빈 테이블 변경 시, 테이블 그룹에 속해있지 않아야 합니다");
		}
		this.empty = empty;
	}

	public void changeNumberOfGuests(int numbers) {
		if (this.isEmpty()) {
			throw new AppException(ErrorCode.WRONG_INPUT, "빈 테이블의 인원을 변경할 수 없습니다");
		}
		this.numberOfGuests = NumberOfGuests.of(numbers);
	}

	public void group(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public void unGroup() {
		this.tableGroup = null;
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public NumberOfGuests getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		OrderTable that = (OrderTable)o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
