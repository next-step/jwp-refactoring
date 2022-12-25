package kitchenpos.order.order.application;

import kitchenpos.order.table.application.OrderTableService;
import kitchenpos.order.table.client.dto.TableDto;

public class OrderValidator {


	private final OrderTableService orderTableService;
	public OrderValidator(OrderTableService orderTableService) {
		this.orderTableService = orderTableService;
	}
	//
	// void validateChangeEmpty(Long orderTableId);
	// void validateUngroup(TableGroup tableGroup);


	public void validateCreateOrder(long tableId) {
		TableDto table = orderTableService.getTable(tableId);
		if (table.isEmpty()) {
			throw new IllegalArgumentException(
				String.format("주문을 하는 테이블(%s)은 비어있을 수 없습니다.", table));
		}
	};
}
