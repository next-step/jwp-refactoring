package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.TableGroupException;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
	private TableGroup tableGroup;

	@Column(nullable = false)
	private Integer numberOfGuests;

	@Column(nullable = false)
	private Boolean empty;

	protected OrderTable() {
	}

	private OrderTable(Integer numberOfGuests, TableGroup tableGroup, Boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.tableGroup = tableGroup;
		this.empty = empty;
	}

	public static OrderTable of(Integer numberOfGuests, Boolean empty) {
		return new OrderTable(numberOfGuests, null, empty);
	}

	public static OrderTable of(Integer numberOfGuests, TableGroup tableGroup, Boolean empty) {
		return new OrderTable(numberOfGuests, tableGroup, empty);
	}

	public Long getId() {
		return id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean isEmpty() {
		return empty;
	}

	public void empty(Boolean empty) {
		if (empty) {
			validateNonNullTableGroup();
		}
		this.empty = empty;
	}

	private void validateNonNullTableGroup() {
		if (Objects.nonNull(tableGroup)) {
			throw new TableGroupException(ErrorCode.ALREADY_HAS_TABLE_GROUP);
		}
	}

	public void changeTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public void changeNumberOfGuests(Integer numberOfGuests) {
		validateNumberOfGuest(numberOfGuests);
		validateIsEmpty();
		this.numberOfGuests = numberOfGuests;
	}

	private void validateNumberOfGuest(Integer numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new OrderException(ErrorCode.NUMBER_OF_GUESTS_IS_POSITIVE_NUMBER);
		}
	}

	private void validateIsEmpty() {
		if (isEmpty()) {
			throw new OrderException(ErrorCode.ORDER_TABLE_IS_NO_EMPTY);
		}
	}

	public OrderTableResponse toResDto() {
		if (tableGroup == null) {
			return OrderTableResponse.of(id, null, numberOfGuests, empty);
		}
		return OrderTableResponse.of(id, tableGroup.getId(), numberOfGuests, empty);
	}

	public void unGroup() {
		changeTableGroup(null);
	}
}
