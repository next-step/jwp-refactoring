package kitchenpos.table.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

	private List<OrderTableId> orderTables;

	private TableGroupRequest() {
	}

	public TableGroupRequest(List<Long> idList) {
		this.orderTables = idList.stream()
			.map(OrderTableId::new)
			.collect(Collectors.toList());
	}

	public List<Long> toOrderTableId() {
		return orderTables.stream()
			.map(OrderTableId::getId)
			.collect(Collectors.toList());
	}

	public List<OrderTableId> getOrderTables() {
		return orderTables;
	}

	static class OrderTableId {
		private Long id;

		private OrderTableId() {
		}

		public OrderTableId(Long id) {
			this.id = id;
		}

		public Long getId() {
			return id;
		}
	}
}
