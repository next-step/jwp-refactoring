package kitchenpos.order.ui.request;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableEmpty;
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
		return TableGroup.from(OrderTables.from(orderTables.stream()
			.map(OrderTableIdRequest::toEntity)
			.collect(Collectors.toList())));
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
			return OrderTable.of(NumberOfGuests.from(0), TableEmpty.from(true));
		}
	}

}
