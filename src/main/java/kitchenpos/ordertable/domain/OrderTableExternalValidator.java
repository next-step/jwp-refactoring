package kitchenpos.ordertable.domain;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.exception.CanNotEditOrderTableEmptyByStatusException;

@Component
public class OrderTableExternalValidator {

	private final OrderService orderService;

	public OrderTableExternalValidator(OrderService orderService) {
		this.orderService = orderService;
	}

	public void changeEmpty(Long orderTableId) {
		if (orderService.existsOrderStatusCookingOrMeal(orderTableId)) {
			throw new CanNotEditOrderTableEmptyByStatusException();
		}
	}
}
