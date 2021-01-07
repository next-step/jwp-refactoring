package kitchenpos.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TableGroupRequest {
	@NotEmpty(message = "주문 테이블 정보가 없습니다.")
	@Size(min = 2, message = "주문 테이블은 최소 2개입니다.")
	private List<Long> orderTableIds;

	private TableGroupRequest() {
	}

	private TableGroupRequest(List<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public static TableGroupRequest of(List<Long> orderTableIds) {
		return new TableGroupRequest(orderTableIds);
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}
}
