package kitchenpos.ordertable.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@Column(name = "table_group_id", columnDefinition = "bigint(20)")
	private Long tableGroupId;

	@Column(columnDefinition = "int(11)", nullable = false)
	private int numberOfGuests;

	@Column(nullable = false)
	private Boolean empty;

	public OrderTable() {
	}

	public OrderTable(Long id) {
		this.id = id;
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this(null, null, numberOfGuests, empty);
	}

	public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public boolean isGrouped() {
		return Objects.nonNull(tableGroupId);
	}

	public boolean isNotUse() {
		return this.empty == true;
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		validateOrderTableIsNotUse();
		this.numberOfGuests = numberOfGuests;
	}

	private void validateOrderTableIsNotUse() {
		if (isNotUse()) {
			throw new IllegalArgumentException("비어 있는 테이블입니다");
		}
	}

	public boolean isUseOrIsGrouped() {
		return isUse() || isGrouped();
	}

	public boolean isUse() {
		return this.empty == false;
	}

	public Long getId() {
		return id;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public Boolean getEmpty() {
		return empty;
	}

	public Long getTableGroupId() {
		if (isNotGrouped()) {
			return -1L;
		}
		return this.tableGroupId;
	}

	private boolean isNotGrouped() {
		return Objects.isNull(tableGroupId);
	}

	public void use() {
		this.empty = false;
	}

	public void toGroup(Long tableGroupId) {
		use();
		this.tableGroupId = tableGroupId;
	}

	public void notUse() {
		this.empty = true;
	}

	public void ungroup() {
		this.tableGroupId = null;
	}

	public void changeStatus(boolean status) {
		if (status) {
			notUse();
			return;
		}
		use();
	}
}
