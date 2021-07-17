package kitchenpos.table.domain;

import javax.persistence.Entity;

import kitchenpos.tableGroup.domain.TableGroup;

@Entity
public class OrderTable {
	private Long id;
	private TableGroup tableGroup;
	private int numberOfGuest;
	private boolean empty;
}
