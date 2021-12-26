package kitchenpos.tablegroup.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.tablegroup.exception.CanNotUngroupByOrderStatusException;

@Component
public class TableGroupExternalValidator {

	private final OrderService orderService;

	public TableGroupExternalValidator(OrderService orderService) {
		this.orderService = orderService;
	}

	public void ungroup(List<Long> orderTableIds) {
		if (orderService.existsOrderStatusCookingOrMeal(orderTableIds)) {
			throw new CanNotUngroupByOrderStatusException();
		}
	}
}
