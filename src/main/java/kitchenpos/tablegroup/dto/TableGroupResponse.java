package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

	private Long id;
	private LocalDateTime createdDate;
	private List<OrderTableResponse> orderTables;

	public TableGroupResponse(Long id, LocalDateTime createdDate,
		  List<OrderTableResponse> orderTables) {
		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;
	}

	public static TableGroupResponse of(OrderTables updatedOrderTables) {
		TableGroup tableGroup = updatedOrderTables.getRespTableGroup();

		return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
			  updatedOrderTables.getOrderTables().stream()
					.map(OrderTableResponse::of)
					.collect(Collectors.toList())
		);
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTableResponse> getOrderTables() {
		return orderTables;
	}
}
