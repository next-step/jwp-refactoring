package kitchenpos.ordertablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ordertable.dto.OrderTableDto;

public class OrderTableGroupCreateRequest {
	private List<OrderTableDto> orderTables;

	public OrderTableGroupCreateRequest() {
	}

	public OrderTableGroupCreateRequest(List<Long> orderTableIds) {
		this.orderTables = orderTableIds.stream()
			.map(id -> new OrderTableDto(id, null, 0, false))
			.collect(Collectors.toList());
	}

	public List<OrderTableDto> getOrderTables() {
		return orderTables;
	}

	public void setOrderTables(List<OrderTableDto> orderTables) {
		this.orderTables = orderTables;
	}

	public TableGroup toOrderTableGroup() {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(
			orderTables.stream()
				.map(ot -> {
					OrderTable orderTable = new OrderTable();
					orderTable.setId(ot.getId());
					return orderTable;
				})
				.collect(Collectors.toList()));
		return tableGroup;
	}
}
