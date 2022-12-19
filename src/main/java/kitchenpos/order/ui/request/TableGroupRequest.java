package kitchenpos.order.ui.request;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupRequest {
	private final List<OrderTableIdRequest> orderTables;

	public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
		this.orderTables = orderTables;
	}

	public List<OrderTableIdRequest> getOrderTables() {
		return orderTables;
	}

	public TableGroup toEntity() {
		return new TableGroup(null, LocalDateTime.now(), orderTables.stream()
			.map(OrderTableIdRequest::toEntity)
			.collect(Collectors.toList()));
	}

	public static class OrderTableIdRequest {
		private final long id;

		public OrderTableIdRequest(long id) {
			this.id = id;
		}

		public long getId() {
			return id;
		}

		public OrderTable toEntity() {
			return new OrderTable(id, null, 0, false);
		}
	}

}
