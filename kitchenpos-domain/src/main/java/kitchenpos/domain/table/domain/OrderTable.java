package kitchenpos.domain.table.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_group_id")
	private TableGroup tableGroup;

	@Embedded
	private OrderCollections orderCollections;

	private int numberOfGuests;
	private boolean empty;

	public OrderTable() {
	}

	public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
		this.tableGroup = tableGroup;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
		this.orderCollections = new OrderCollections();
	}

	public OrderTable(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
		this.orderCollections = new OrderCollections();
	}

	public OrderTable(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
		this.empty = true;
		this.orderCollections = new OrderCollections();
	}

	public OrderTable(boolean empty) {
		this.empty = empty;
		this.orderCollections = new OrderCollections();
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

	public boolean isEmpty() {
		return empty;
	}

	public void changeTableGroup(TableGroup tableGroup) {
		this.tableGroup = tableGroup;
	}

	public void changeEmpty(boolean empty) {
		if (Objects.nonNull(tableGroup)) {
			throw new IllegalArgumentException();
		}
		validateOrderTableStatusToChangeEmpty();
		this.empty = empty;
	}

	private void validateOrderTableStatusToChangeEmpty() {
		if(this.empty){
			throw new IllegalArgumentException();
		}
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		if (numberOfGuests < 0) {
			throw new IllegalArgumentException();
		}
		this.numberOfGuests = numberOfGuests;
	}

	public void ungroup() {
		if(!orderCollections.isStatusCompletion()){
			throw new IllegalArgumentException();
		}
		tableGroup = null;
	}

}
