package kitchenpos.order.ui.request;

import java.util.List;

public class TableGroupRequest {
	private final List<OrderTableIdRequest> orderTables;

	public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
		this.orderTables = orderTables;
	}

	public List<OrderTableIdRequest> getOrderTables() {
		return orderTables;
	}

	public static class OrderTableIdRequest {
		private final long id;

		public OrderTableIdRequest(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}
	}
}
