package kitchenpos.web.table.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.table.domain.OrderTable;

/**
 * @author : byungkyu
 * @date : 2021/01/24
 * @description :
 **/
public class OrderTableResponse {
	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;

	public OrderTableResponse() {
	}

	public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
		this.id = id;
		this.tableGroupId = tableGroupId;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public static List<OrderTableResponse> of(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(OrderTableResponse::of)
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public static OrderTableResponse of (OrderTable orderTable){
		Long orderTableGroupId = null;
		if(orderTable.getTableGroup() != null){
			orderTableGroupId = orderTable.getId();
		}
		return new OrderTableResponse(orderTable.getId(), orderTableGroupId, orderTable.getNumberOfGuests(),
			orderTable.isEmpty());
	}
}
