package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.tableGroup.domain.TableGroup;

@Entity
public class OrderTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
	private TableGroup tableGroup;

	@Embedded
	private NumberOfGuests numberOfGuests;


	@Column(nullable = false)
	private boolean empty;

	public OrderTable() {
	}

	public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
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

	public boolean isTableGrouped() {
		return Objects.nonNull(tableGroup);
	}

	public void updateEmpty(boolean empty) {
		this.empty = empty;
	}

	public void updateNumberOfGuests(NumberOfGuests numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public void unGroup() {
		this.tableGroup = null;
	}

	public void mapTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}
}
