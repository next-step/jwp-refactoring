package kitchenpos.order.ui.request;

public class TableStatusRequest {

	private final String tableStatus;

	public TableStatusRequest(String tableStatus) {
		this.tableStatus = tableStatus;
	}

	public String getTableStatus() {
		return tableStatus;
	}
}
