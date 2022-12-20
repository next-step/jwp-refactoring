package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TableEmpty {

	@Column
	private boolean empty;

	protected TableEmpty() {
	}

	private TableEmpty(boolean empty) {
		this.empty = empty;
	}

	public static TableEmpty from(boolean empty) {
		return new TableEmpty(empty);
	}

	public boolean isEmpty() {
		return empty;
	}
}
