package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore //memo [2021-01-4 22:37] 수정필요
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;
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

	public void setId(final Long id) {
		this.id = id;
	}

	public TableGroup getTableGroup() {
		return tableGroup;
	}

	public void setTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(final int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(final boolean empty) {
		this.empty = empty;
	}
}
