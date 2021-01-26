package kitchenpos.table.dto;

import java.util.List;

/**
 * @author : byungkyu
 * @date : 2021/01/24
 * @description :
 **/
public class TableGroupRequest {
	List<Long> orderTableIds;

	public TableGroupRequest() {
	}

	public TableGroupRequest(List<Long> orderTableIds) {
		this.orderTableIds = orderTableIds;
	}

	public List<Long> getOrderTableIds() {
		return orderTableIds;
	}
}
