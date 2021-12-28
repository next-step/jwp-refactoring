package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tablegroup.exception.TableGroupException;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long tableGroupId;

	@Column(nullable = false)
	private Integer numberOfGuests;

	@Column(nullable = false)
	private Boolean empty;

	protected OrderTable() {
	}

	private OrderTable(Integer numberOfGuests, Long tableGroupId, Boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.tableGroupId = tableGroupId;
		this.empty = empty;
	}

	public static OrderTable of(Integer numberOfGuests, Boolean empty) {
		return new OrderTable(numberOfGuests, null, empty);
	}

	public static OrderTable of(Integer numberOfGuests, Long tableGroupId, Boolean empty) {
		return new OrderTable(numberOfGuests, tableGroupId, empty);
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
		if (Objects.nonNull(tableGroupId)) {
			throw new TableGroupException(ErrorCode.ALREADY_HAS_TABLE_GROUP);
		}
	}

	public void changeTableGroup(Long tableGroupId) {
		this.tableGroupId = tableGroupId;
		this.empty = false;
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

	public void unGroup() {
		changeTableGroup(null);
	}
}
